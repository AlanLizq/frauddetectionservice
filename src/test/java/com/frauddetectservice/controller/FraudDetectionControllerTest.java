package com.frauddetectservice.controller;

import com.frauddetectservice.model.Transaction;
import com.frauddetectservice.mq.MessagePublisherService;
import com.frauddetectservice.response.ApiResponse;
import com.frauddetectservice.service.DetectionService;
import com.frauddetectservice.logging.GoogleCloudLoggingService;  // 引入 GcpLoggingService
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
    private GoogleCloudLoggingService mockGoogleCloudLoggingService;

    @Mock
    private MessagePublisherService mockMessagePublisherService;

    @InjectMocks
    private FraudDetectionController fraudDetectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(fraudDetectionController).build();
    }

    @Test
    void testCheckFraud_ValidTransaction() throws Exception {
        ApiResponse<String> mockSuccess = mock(ApiResponse.class);
        when(mockSuccess.isSuccess()).thenReturn(true);
        when(mockSuccess.getStatus()).thenReturn(200);
        when(mockSuccess.getData()).thenReturn("Transaction is valid and safe");
        when(mockSuccess.getError()).thenReturn(null);
        when(mockDetectionService.detectFraud(any(Transaction.class))).thenReturn("Transaction is valid and safe");

        mockMvc.perform(post("/fraud/detect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"serwr500\",\"accountId\":\"acc123\",\"amount\":100,\"country\":\"CN\",\"ipAddr\":\"192.168.4.5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":200,\"success\":true,\"data\":\"Transaction is valid and safe\",\"error\":null}"));

        verify(mockDetectionService, times(1)).detectFraud(any(Transaction.class));

        verify(mockGoogleCloudLoggingService, times(1)).info(Mockito.anyString());
        verify(mockGoogleCloudLoggingService, times(0)).warn(Mockito.anyString());
        verify(mockMessagePublisherService, times(0)).publishTransaction(any(Transaction.class));
    }

    @Test
    void testCheckFraud_FraudulentTransaction() throws Exception {
        ApiResponse<String> mockError = mock(ApiResponse.class);
        when(mockError.isSuccess()).thenReturn(true);
        when(mockError.getStatus()).thenReturn(200);
        when(mockError.getData()).thenReturn("Fraudulent Transaction");
        when(mockError.getError()).thenReturn(null);
        when(mockDetectionService.detectFraud(any(Transaction.class))).thenReturn("Fraudulent Transaction");

        mockMvc.perform(post("/fraud/detect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"wew234\",\"accountId\":\"bbb444\",\"amount\":2200,\"country\":\"CN\",\"ipAddr\":\"192.168.4.5\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":200,\"success\":true,\"data\":\"Fraudulent Transaction\",\"error\":null}"));

        verify(mockDetectionService, times(1)).detectFraud(any(Transaction.class));
        verify(mockGoogleCloudLoggingService, times(1)).info(Mockito.anyString());
        verify(mockGoogleCloudLoggingService, times(1)).warn(Mockito.anyString());
        verify(mockMessagePublisherService, times(1)).publishTransaction(any(Transaction.class));
    }
}
