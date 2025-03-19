/*package com.frauddetectservice.rules;

import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RuleEngineTest {

    private RuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new RuleEngine();
    }

    @Test
    void testRegisterRule_SortingByPriority() {
        // 创建两个规则，分别设置不同优先级
        Rule highPriorityRule = new ThresholdRule(new BigDecimal("10000"));
        highPriorityRule.setPriority(1); // 高优先级

        Rule lowPriorityRule = new WhitelistRule(Set.of("12345"));
        lowPriorityRule.setPriority(10); // 低优先级

        ruleEngine.registerRule(lowPriorityRule);
        ruleEngine.registerRule(highPriorityRule);

        // 模拟交易
        Transaction transaction = new Transaction();
        transaction.setTransactionId("67890");
        transaction.setAmount(new BigDecimal("15000"));

        // 应优先匹配高优先级规则
        String result = ruleEngine.executeRules(transaction);
        assertEquals("ThresholdRule", result);
    }

    @Test
    void testExecuteRules_FraudulentTransaction() {
        ruleEngine.registerRule(new ThresholdRule(new BigDecimal("10000")));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("67890");
        transaction.setAmount(new BigDecimal("15000"));

        String result = ruleEngine.executeRules(transaction);
        assertEquals("ThresholdRule", result);
    }

    @Test
    void testExecuteRules_WhitelistedTransaction() {
        // 注册规则
        ruleEngine.registerRule(new WhitelistRule(Set.of("acc123", "acc456")));

        // 构造交易数据
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acc123"); // 白名单中的账户ID

        // 验证规则匹配
        String result = ruleEngine.executeRules(transaction);
        assertEquals("WhitelistRule", result, "交易应匹配白名单规则");
    }


    @Test
    void testExecuteRules_NonFraudulentTransaction() {
        ruleEngine.registerRule(new ThresholdRule(new BigDecimal("10000")));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("67891");
        transaction.setAmount(new BigDecimal("5000"));

        String result = ruleEngine.executeRules(transaction);
        assertNull(result);
    }

    @Test
    void testExecuteRules_EmptyRuleEngine() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("67892");
        transaction.setAmount(new BigDecimal("15000"));

        String result = ruleEngine.executeRules(transaction);
        assertNull(result);
    }
}*/
