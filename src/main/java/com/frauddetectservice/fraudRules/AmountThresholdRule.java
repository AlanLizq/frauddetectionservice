package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

import java.math.BigDecimal;

// 金额阈值规则
public class AmountThresholdRule extends FraudRule {
    private long threshold;
    private String ruleName;

    public AmountThresholdRule(String name, int priority, String type, long threshold) {
        super(name, priority, type);
        this.threshold = threshold;
        this.ruleName = name;
    }

    @Override
    public String evaluate(Transaction transaction) {
        if(transaction.getAmount() > threshold){

            return String.format("Transaction-id %s , fraud rule {%s}", transaction.getTransactionId(), ruleName);
        }

        return null;
    }
}

