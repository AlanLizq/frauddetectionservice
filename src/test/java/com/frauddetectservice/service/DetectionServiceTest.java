package com.frauddetectservice.service;

import com.frauddetectservice.fraudRules.RuleEngine;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

class DetectionServiceTest {

    private DetectionService detectionService;

    @BeforeEach
    void setUp() {
        // 模拟 RuleLoaderService
        RuleEngine ruleEngine = mock(RuleEngine.class);

        // 初始化 FraudDetectionService
        detectionService = new DetectionService(ruleEngine);
    }
/*
    @Test
    void testDetectFraud() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acc123");

        String result = fraudDetectionService.detectFraud(transaction);
        assertEquals("empty rules", result, "当规则为空时，应返回 'empty rules'");
    }*/
}
