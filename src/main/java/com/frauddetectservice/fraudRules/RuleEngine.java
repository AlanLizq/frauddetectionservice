package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

// 规则引擎
@Service
public class RuleEngine {
    private RuleChainNode chainHead;
    @Autowired
    private YamlRuleLoader ruleLoader;

    @PostConstruct
    public void initRules() {
        List<FraudRule> rules = ruleLoader.loadRules();

        // 按优先级排序
        rules.sort(Comparator.comparingInt(FraudRule::getPriority));

        // 构建责任链
        RuleChainNode prev = null;
        for (FraudRule rule : rules) {
            if(rule.getType().startsWith("custom-")){
                continue;
            }
            RuleChainNode node = new RuleChainNode(rule);
            if (prev != null) {
                prev.setNext(node);
            } else {
                chainHead = node;
            }
            prev = node;
        }
    }

    public String checkFraud(Transaction transaction) {
        if(chainHead != null){
            return chainHead.check(transaction);
        }
        return null;
    }
}

