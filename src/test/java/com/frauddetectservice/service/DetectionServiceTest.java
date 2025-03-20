package com.frauddetectservice.service;

import com.frauddetectservice.fraudRules.RuleEngine;
import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class DetectionServiceTest {

    private DetectionService detectionService;

    @BeforeEach
    void setUp() {
        RuleEngine ruleEngine = mock(RuleEngine.class);
        detectionService = new DetectionService(ruleEngine);
    }

    @Test
    void testDetectFraud() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("we1231");
        transaction.setAccountId("acc456");
        transaction.setAmount(18000);
        transaction.setCountry("CN");
        transaction.setIpAddr("192.168.1.1");

        String result = detectionService.detectFraud(transaction);
        assertEquals("Transaction is valid and safe","Transaction is valid and safe", result);
    }
}
