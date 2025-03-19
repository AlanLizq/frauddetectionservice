package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

// 地理位置规则
public class LocationRule extends FraudRule {
    private String ruleName;
    public LocationRule(String name, int priority, String type) {
        super(name, priority, type);
        this.ruleName = name;
    }

    @Override
    public String evaluate(Transaction transaction) {
        // 假设非中国账户触发欺诈检测
        if( !transaction.getCountry().equals("CN")){
            return ruleName;
        }
        return null;
    }
}
