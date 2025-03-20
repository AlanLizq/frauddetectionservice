package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

public class RuleChainNode {
    private final FraudRule rule;
    private RuleChainNode next;

    public RuleChainNode(FraudRule rule) {
        this.rule = rule;
    }

    public String check(Transaction transaction) {
        String currentResult = rule.evaluate(transaction);
        if (currentResult ==null && next != null) {
            return next.check(transaction);
        }
        return currentResult;
    }

    public void setNext(RuleChainNode next) {
        this.next = next;
    }
}