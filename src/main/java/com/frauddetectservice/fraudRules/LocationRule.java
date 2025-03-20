package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

public class LocationRule extends FraudRule {
    private String ruleName;
    public LocationRule(String name, int priority, String type) {
        super(name, priority, type);
        this.ruleName = name;
    }

    @Override
    public String evaluate(Transaction transaction) {
        // none China is illegal
        if( !transaction.getCountry().equals("CN")){
            return ruleName;
        }
        return null;
    }
}
