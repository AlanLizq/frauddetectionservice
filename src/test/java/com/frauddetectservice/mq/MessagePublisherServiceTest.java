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
        Transaction transaction = new Transaction();
        transaction.setTransactionId("we1231");
        transaction.setAccountId("acc456");
        transaction.setAmount(18000);
        transaction.setCountry("CN");
        transaction.setIpAddr("192.168.1.1");

        assertDoesNotThrow(() -> messagePublisherService.publish(transaction.getTransactionId()));
        System.out.println("Test for publishMessage passed.");
    }
}
