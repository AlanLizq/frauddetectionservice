package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

// 规则链节点
public class RuleChainNode {
    private final FraudRule rule;
    private RuleChainNode next;

    public RuleChainNode(FraudRule rule) {
        this.rule = rule;
    }

    public String check(Transaction transaction) {
        String currentResult = rule.evaluate(transaction);
        if (currentResult ==null && next != null) {
            return next.check(transaction); // 责任链传递,currentResult ==null 表示当前rule检测pass, 需要传递检查下一个rule
        }
        return currentResult;
    }

    // 设置下一个节点
    public void setNext(RuleChainNode next) {
        this.next = next;
    }
}