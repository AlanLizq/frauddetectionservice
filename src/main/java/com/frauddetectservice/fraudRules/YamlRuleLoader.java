package com.frauddetectservice.fraudRules;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class YamlRuleLoader {

    // 规则配置文件路径
    @Value("classpath:fraud-rules/rules-config.yml")
    private Resource rulesResource;

    // YAML解析器
    private final Yaml yaml;

    public YamlRuleLoader() {
        // 初始化 YAML 解析器
        LoaderOptions loaderOptions = new LoaderOptions();
        this.yaml = new Yaml(
                new Constructor(RuleConfig.class, loaderOptions)
        );
    }

    // 规则工厂映射
    private static final Map<String, BiFunction<RuleConfig.RuleDefinition, Map<String, FraudRule>, FraudRule>>
            RULE_FACTORIES = Map.of(
            "amount", YamlRuleLoader::createAmountRule,
            "account", YamlRuleLoader::createAccountRule,
            "composite", YamlRuleLoader::createCompositeRule,
            "custom-location", YamlRuleLoader::createLocationRule,
            "custom-ip", YamlRuleLoader::createIpRule
    );

    public List<FraudRule> loadRules() {
        try (InputStream inputStream = rulesResource.getInputStream()) {
            // 1. 加载并解析YAML配置
            RuleConfig config = yaml.load(inputStream);

            // 2. 创建基础规则集合（先创建非组合规则）
            Map<String, FraudRule> ruleMap = new HashMap<>();
            for (RuleConfig.RuleDefinition def : config.getRules()) {
                if (!"composite".equals(def.getType())) {
                    FraudRule rule = createRule(def, ruleMap);
                    ruleMap.put(def.getName(), rule);
                }
            }

            // 3. 创建组合规则（依赖其他规则）
            for (RuleConfig.RuleDefinition def : config.getRules()) {
                if ( "composite".equals(def.getType())) {
                    FraudRule rule = createRule(def, ruleMap);
                    ruleMap.put(def.getName(), rule);
                }
            }

            // 4. 按优先级排序
            return ruleMap.values().stream()
                    .sorted(Comparator.comparingInt(FraudRule::getPriority))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println(String.format("Failed to load rules configuration"));
            throw new IllegalStateException("Rules configuration loading failed", e);
        }
    }

    private static FraudRule createRule(RuleConfig.RuleDefinition def,
                                        Map<String, FraudRule> existingRules) {
        return RULE_FACTORIES.getOrDefault(def.getType(), (d, m) -> {
            throw new IllegalArgumentException("Unknown rule type: " + def.getType());
        }).apply(def, existingRules);
    }

    private static FraudRule createAmountRule(RuleConfig.RuleDefinition def,
                                              Map<String, FraudRule> existingRules) {
        long threshold = def.getParams().get("threshold") instanceof Number num ?
                num.longValue() :
                Long.valueOf(def.getParams().get("threshold").toString());

        return new AmountThresholdRule(
                def.getName(),
                def.getPriority(),
                def.getType(),
                threshold
        );
    }

    private static FraudRule createAccountRule(RuleConfig.RuleDefinition def,
                                               Map<String, FraudRule> existingRules) {
        String sourcePath = (String) def.getParams().get("source");
        try {
            Resource resource = new ClassPathResource(sourcePath);
            return new AccountBlacklistRule(def.getName(), def.getPriority(), def.getType(),resource);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid account rule source: " + sourcePath, e);
        }
    }

    private static FraudRule createLocationRule(RuleConfig.RuleDefinition def,
                                              Map<String, FraudRule> existingRules) {

        return new LocationRule(def.getName(), def.getPriority(), def.getType());
    }

    private static FraudRule createIpRule(RuleConfig.RuleDefinition def,
                                                Map<String, FraudRule> existingRules) {

        return new IpRule(def.getName(), def.getPriority(), def.getType());
    }

    @SuppressWarnings("unchecked")
    private static FraudRule createCompositeRule(RuleConfig.RuleDefinition def,
                                                 Map<String, FraudRule> existingRules) {
        CompositeType operator = CompositeType.valueOf(
                ((String) def.getParams().get("operator")).toUpperCase()
        );

        List<String> childRuleNames = (List<String>) def.getParams().get("rules");
        List<FraudRule> childRules = childRuleNames.stream()
                .map(name -> {
                    FraudRule rule = existingRules.get(name);
                    if (rule == null) {
                        throw new IllegalArgumentException("Undefined child rule: " + name);
                    }
                    return rule;
                })
                .collect(Collectors.toList());

        return new CompositeRule(
                def.getName(),
                def.getPriority(),
                operator,
                def.getType(),
                childRules.toArray(new FraudRule[0])
        );
    }

    // YAML配置映射类
    public static class RuleConfig {
        private List<RuleDefinition> rules;
        public RuleConfig() {}
        @Data
        public static class RuleDefinition {
            private String name;
            private String type;
            private int priority;
            private Map<String, Object> params;
            public RuleDefinition() {}
        }

        // Getters and setters
        public List<RuleDefinition> getRules() { return rules; }
        public void setRules(List<RuleDefinition> rules) { this.rules = rules; }
    }
}
