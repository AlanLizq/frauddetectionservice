package com.frauddetectservice.controller;

import com.frauddetectservice.model.Transaction;
import com.frauddetectservice.mq.MessagePublisherService;
import com.frauddetectservice.service.DetectionService;
import com.frauddetectservice.logging.GcpLoggingService;  // 引入 GcpLoggingService
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FraudDetectionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DetectionService mockDetectionService;

    @Mock
    private GcpLoggingService mockGcpLoggingService;  // 模拟 GcpLoggingService

    @Mock
    private MessagePublisherService mockMessagePublisherService;  // 模拟 GCPPubSubService

    @InjectMocks
    private FraudDetectionController fraudDetectionController;  // 控制器

    @BeforeEach
    void setUp() {
        // 初始化 Mockito 的 @Mock 注解的对象
        MockitoAnnotations.initMocks(this);

        // 初始化 MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(fraudDetectionController).build();
    }

    @Test
    void testCheckFraud_ValidTransaction() throws Exception {
        // 模拟 FraudDetectionService 的行为
        when(mockDetectionService.detectFraud(any(Transaction.class))).thenReturn("Transaction is safe");

        // 执行请求并验证结果
        mockMvc.perform(post("/fraud/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"txn1\",\"accountId\":\"acc123\",\"amount\":100.50}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction is safe"));

        // 验证 FraudDetectionService 的行为
        verify(mockDetectionService, times(1)).detectFraud(any(Transaction.class));

        // 验证日志记录
        verify(mockGcpLoggingService, times(1)).info(Mockito.anyString());  // 确保 info 方法被调用
        verify(mockGcpLoggingService, times(0)).warn(Mockito.anyString());  // 确保没有调用 warn 方法

        // 验证 GCPPubSubService 的行为
        verify(mockMessagePublisherService, times(0)).publishTransaction(any(Transaction.class));  // 确保没有调用 publishTransaction 方法
    }

    @Test
    void testCheckFraud_FraudulentTransaction() throws Exception {
        // 模拟 FraudDetectionService 的行为
        when(mockDetectionService.detectFraud(any(Transaction.class))).thenReturn("Fraudulent Transaction");

        // 执行请求并验证结果
        mockMvc.perform(post("/fraud/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"txn1\",\"accountId\":\"acc123\",\"amount\":1000.50}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fraudulent Transaction"));

        // 验证 FraudDetectionService 的行为
        verify(mockDetectionService, times(1)).detectFraud(any(Transaction.class));

        // 验证日志记录
        verify(mockGcpLoggingService, times(1)).info(Mockito.anyString());  // 确保 info 方法被调用
        verify(mockGcpLoggingService, times(1)).warn(Mockito.anyString());  // 确保 warn 方法被调用

        // 验证 GCPPubSubService 的行为
        verify(mockMessagePublisherService, times(1)).publishTransaction(any(Transaction.class));  // 确保 publishTransaction 方法被调用
    }
}
