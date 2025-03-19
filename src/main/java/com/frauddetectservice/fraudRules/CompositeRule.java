package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 组合规则实现
public class CompositeRule extends FraudRule {
    private CompositeType compositeType;
    private FraudRule[] childRules;

    public CompositeRule(String name, int priority,
                         CompositeType compositeType, String type, FraudRule... childRules) {
        super(name, priority, type);
        this.compositeType = compositeType;
        this.childRules = childRules;
    }

    @Override
    public String evaluate(Transaction transaction) {
        if (compositeType == CompositeType.AND) {
            return allMatchCheck(transaction);
        } else {
            return anyMatchCheck(transaction);
        }
    }

    private String allMatchCheck(Transaction transaction){
        List<String> result = new ArrayList<>();
        for (FraudRule rule:childRules
             ) {
            String checkRes = rule.evaluate(transaction);
            if(checkRes != null){
                result.add(checkRes);
            }
        }

        if(result.size() == childRules.length){
            return String.format("Transaction-id %s , fraud rule {%s}", transaction.getTransactionId(), String.join("#", result));
        }

        return null;
    }

    private String anyMatchCheck(Transaction transaction){
        List<String> result = new ArrayList<>();
        for (FraudRule rule:childRules
        ) {
            String checkRes = rule.evaluate(transaction);
            if(checkRes != null){
                result.add(checkRes);
            }
        }

        if(result.size() > 0){
            return String.format("Transaction-id %s , fraud rule {%s}", transaction.getTransactionId(), String.join("#", result));
        }

        return null;
    }
}