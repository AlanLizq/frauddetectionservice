/*package com.frauddetectservice.rules;

import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WhitelistRuleTest {

    @Test
    void testWhitelistRuleMatches() {
        WhitelistRule rule = new WhitelistRule(Set.of("acc123", "acc456"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acc123");

        // 验证匹配白名单账户的交易
        assertTrue(rule.matches(transaction), "白名单中的账户应匹配规则");
    }

    @Test
    void testWhitelistRuleDoesNotMatch() {
        WhitelistRule rule = new WhitelistRule(Set.of("acc123", "acc456"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn2");
        transaction.setAccountId("acc789");

        // 验证不匹配白名单账户的交易
        assertFalse(rule.matches(transaction), "不在白名单中的账户不应匹配规则");
    }

    @Test
    void testWhitelistRuleWithNullTransaction() {
        WhitelistRule rule = new WhitelistRule(Set.of("acc123", "acc456"));

        // 验证空交易时不匹配规则
        assertFalse(rule.matches(null), "空交易不应匹配规则");
    }

    @Test
    void testWhitelistRuleWithNullAccountId() {
        WhitelistRule rule = new WhitelistRule(Set.of("acc123", "acc456"));

        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn3");
        transaction.setAccountId(null);

        // 验证账户ID为空时不匹配规则
        assertFalse(rule.matches(transaction), "账户ID为空的交易不应匹配规则");
    }

    @Test
    void testWhitelistRuleThrowsExceptionForEmptyWhitelist() {
        // 验证空白名单时抛出异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new WhitelistRule(Set.of()));
        assertEquals("Whitelist cannot be null or empty", exception.getMessage());
    }

    @Test
    void testWhitelistRulePriority() {
        WhitelistRule rule = new WhitelistRule(Set.of("acc123", "acc456"));
        rule.setPriority(2); // 设置优先级

        // 验证优先级设置正确
        assertEquals(2, rule.getPriority(), "规则的优先级应为 2");
    }
}*/
