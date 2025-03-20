package com.frauddetectservice.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {

    @Test
    void testTransactionGettersAndSetters() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId("ere234");
        transaction.setAccountId("abb324");
        transaction.setAmount(15000);
        transaction.setCountry("CN");
        transaction.setIpAddr("192.168.1.1");

        assertEquals("ere234", transaction.getTransactionId());
        assertEquals("abb324", transaction.getAccountId());
        assertEquals(15000, transaction.getAmount());
        assertEquals("CN", transaction.getCountry());
        assertEquals("192.168.1.1", transaction.getIpAddr());
    }
}
