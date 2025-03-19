/*package com.frauddetectservice.rules;

import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ThresholdRuleTest {

    @Test
    void testThresholdRuleMatches() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acc123");
        transaction.setAmount(BigDecimal.valueOf(15000));

        // 验证交易金额大于阈值时匹配规则
        assertTrue(rule.matches(transaction), "交易金额应匹配阈值规则");
    }

    @Test
    void testThresholdRuleDoesNotMatch() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn2");
        transaction.setAccountId("acc456");
        transaction.setAmount(BigDecimal.valueOf(5000));

        // 验证交易金额小于阈值时不匹配规则
        assertFalse(rule.matches(transaction), "交易金额不应匹配阈值规则");
    }

    @Test
    void testThresholdRuleMatchesBoundary() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn3");
        transaction.setAccountId("acc789");
        transaction.setAmount(BigDecimal.valueOf(10000)); // 恰好等于阈值

        // 验证交易金额等于阈值时不匹配规则
        assertFalse(rule.matches(transaction), "交易金额等于阈值时不应匹配规则");
    }

    @Test
    void testThresholdRuleWithNegativeAmount() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn4");
        transaction.setAccountId("acc000");
        transaction.setAmount(BigDecimal.valueOf(-5000)); // 负数金额

        // 验证负数金额时不匹配规则
        assertFalse(rule.matches(transaction), "负数金额的交易不应匹配规则");
    }

   /* @Test
    void testThresholdRuleWithNullAmount() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn5");
        transaction.setAccountId("acc111");
        transaction.setAmount(null); // 空金额

        // 验证金额为空时抛出异常
        Exception exception = assertThrows(NullPointerException.class, () -> rule.matches(transaction));
        assertEquals("Transaction amount cannot be null", exception.getMessage());
    }*/
/*
    @Test
    void testThresholdRulePriority() {
        ThresholdRule rule = new ThresholdRule(BigDecimal.valueOf(10000));
        rule.setPriority(5); // 动态设置优先级

        // 验证优先级设置正确
        assertEquals(5, rule.getPriority(), "规则的优先级应正确设置和返回");
    }
}*/
