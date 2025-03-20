package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// 账户黑名单规则
public class AccountBlacklistRule extends FraudRule {
    private Set<String> blacklist;
    private final Resource resource;
    private String ruleName;

    public AccountBlacklistRule(String name, int priority, String type,
                                @Value("${fraud.rules.account.blacklist-path:classpath:fraud-rules/suspicious-accounts.txt}")
                                Resource resource) {
        super(name, priority, type);
        this.ruleName = name;
        this.resource = resource;
        // 从文件/数据库加载黑名单
        this.blacklist = loadBlacklist();
    }

    @Override
    public String evaluate(Transaction transaction) {
        if(blacklist.contains(transaction.getAccountId())){
            return String.format("Transaction-id: %s , fraud rule {%s}", transaction.getTransactionId(), ruleName);
        }

        return null;
    }

    private Set<String> loadBlacklist() {
        Set<String> blacklist = new HashSet<>();

        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("#")) {
                    blacklist.add(line.trim());
                }
            }

            System.out.println(String.format("Loaded %s blacklisted accounts", blacklist.size()));
        } catch (IOException e) {
            System.out.println(String.format("Failed to load blacklist file: %s", e.getStackTrace()));
            throw new IllegalStateException("Blacklist file loading failed", e);
        } catch (NullPointerException e) {
            System.out.println("Blacklist file not found");
            throw new IllegalStateException("Blacklist file not found", e);
        }

        return Collections.unmodifiableSet(blacklist);
    }
}
