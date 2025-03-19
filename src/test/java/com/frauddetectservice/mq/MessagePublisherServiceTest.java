package com.frauddetectservice.mq;

import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class MessagePublisherServiceTest {

    @Autowired
    private MessagePublisherService messagePublisherService;

    @Test
    void testPublishMessage() {
        // 创建测试用的交易数据
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn123");
        transaction.setAccountId("acc456");
        transaction.setAmount(15000);

        // 验证发布不会抛出异常
        assertDoesNotThrow(() -> messagePublisherService.publish(transaction.getTransactionId()));
        System.out.println("Test for publishMessage passed.");
    }
}
