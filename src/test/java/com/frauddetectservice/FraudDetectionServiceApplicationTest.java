package com.frauddetectservice;

import com.frauddetectservice.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FraudDetectionServiceApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate; // 用于发送 HTTP 请求

    @BeforeEach
    void setUp() {
        // 可在此处进行一些初始化操作，比如数据库清理等
    }

    @Test
    void testCheckFraud_WhitelistRuleTransaction() {
        // 创建一个正常的交易请求
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acc123");
        transaction.setAmount(100);

        // 发送 HTTP 请求，并获取响应
        ResponseEntity<String> response = restTemplate.postForEntity("/fraud/check", transaction, String.class);

        // 验证 HTTP 响应状态码和响应内容
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fraudulent Transaction Detected by Rule: WhitelistRule", response.getBody());
    }
    @Test
    void testCheckFraud_ValidTransaction() {
        // 创建一个正常的交易请求
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn1");
        transaction.setAccountId("acd123");
        transaction.setAmount(100);

        // 发送 HTTP 请求，并获取响应
        ResponseEntity<String> response = restTemplate.postForEntity("/fraud/check", transaction, String.class);

        // 验证 HTTP 响应状态码和响应内容
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction is Legitimate", response.getBody());
    }


    @Test
    void testCheckFraud_ThresholdRuleFraudulentTransaction() {
        // 创建一个欺诈交易请求
        Transaction transaction = new Transaction();
        transaction.setTransactionId("txn2");
        transaction.setAccountId("acc124");
        transaction.setAmount(10000);

        // 发送 HTTP 请求，并获取响应
        ResponseEntity<String> response = restTemplate.postForEntity("/fraud/check", transaction, String.class);

        // 验证 HTTP 响应状态码和响应内容
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fraudulent Transaction Detected by Rule: ThresholdRule", response.getBody());
    }


}
