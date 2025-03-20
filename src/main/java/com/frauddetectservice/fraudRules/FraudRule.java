package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;
import lombok.Data;

@Data
public abstract class FraudRule {
    private String name;
    private int priority; // 优先级数值越小越高
    private String type;

    public FraudRule(String name, int priority, String type) {
        this.name = name;
        this.priority = priority;
        this.type = type;
    }

    public abstract String evaluate(Transaction transaction);

}
