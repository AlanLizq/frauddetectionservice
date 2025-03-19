package com.frauddetectservice.service;

import com.frauddetectservice.model.Transaction;
import com.frauddetectservice.fraudRules.RuleEngine;
import org.springframework.stereotype.Service;

@Service
public class DetectionService {
    private final RuleEngine ruleEngine;

    public DetectionService(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public String detectFraud(Transaction transaction) {
        String paramCheckResult = checkTransaction(transaction);
        if(paramCheckResult != null){// transaction 信息不完整
            return paramCheckResult;
        }

        String result = ruleEngine.checkFraud(transaction);
        if (result != null) {
            return "Fraudulent Transaction is Detected by Rule: " + result;
        }
        return "Transaction is valid and safe";
    }

    private String checkTransaction(Transaction transaction){
        if(transaction.getAccountId() == null){
            return "transaction account id can't be null";
        } else if (transaction.getAmount() == 0L) {
            return "transaction amount must be > 0";
        } else if (transaction.getTransactionId() == null) {
            return "transaction id can't be null";
        } else if (transaction.getCountry() == null){
            return "transaction country can't be null";
        } else if (transaction.getIpAddr() == null){
            return "transaction ip address can't be null";
        } else {
            return null;
        }
    }
}
