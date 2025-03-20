package com.frauddetectservice.fraudRules;

import com.frauddetectservice.model.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IpRule extends FraudRule {
    private String ruleName;
    public IpRule(String name, int priority, String type) {
        super(name, priority, type);
        this.ruleName = name;
    }

    @Override
    public String evaluate(Transaction transaction) {
        //the ip not in white list is illegal
        if(!isChinaIp(transaction.getIpAddr())){
            return ruleName;
        }
        return null;
    }

    private boolean isChinaIp(String ip){
        List<String> ipSet = new ArrayList<>();
        ipSet.add("192.168.3.5");
        ipSet.add("192.168.4.5");
        ipSet.add("192.168.5.5");
        ipSet.add("192.168.6.5");
        ipSet.add("192.168.7.5");

        return ipSet.contains(ip);
    }
}