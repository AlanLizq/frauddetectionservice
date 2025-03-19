package com.frauddetectservice.controller;

import com.frauddetectservice.model.Transaction;
import com.frauddetectservice.mq.MessagePublisherService;
import com.frauddetectservice.response.ApiResponse;
import com.frauddetectservice.service.DetectionService;
import com.frauddetectservice.logging.GcpLoggingService;  // 引入 GcpLoggingService
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

@RestController
@RequestMapping("/fraud")
public class FraudDetectionController {

    private final DetectionService detectionService;
    private final GcpLoggingService gcpLoggingService;  // 添加 GcpLoggingService
    private final MessagePublisherService messagePublisherService;  // 注入 GCPPubSubService

    // 修改构造函数，注入 GcpLoggingService 和 GCPPubSubService
    public FraudDetectionController(DetectionService detectionService,
                                    GcpLoggingService gcpLoggingService,
                                    MessagePublisherService messagePublisherService) {
        this.detectionService = detectionService;
        this.gcpLoggingService = gcpLoggingService;  // 初始化 GcpLoggingService
        this.messagePublisherService = messagePublisherService;    // 初始化 GCPPubSubService
    }

    @PostMapping("/detect")
    public ApiResponse<?> detectFraud(@RequestBody Transaction transaction) {
        try {
            // 在处理交易时记录日志
            gcpLoggingService.info("Received transaction for fraud check: " + transaction);

            String result = detectionService.detectFraud(transaction);

            // 如果检测到欺诈交易，发送告警
            if (result.startsWith("Fraudulent")) {
                messagePublisherService.publish(transaction.getTransactionId() + " | Metadata: " + result);
                gcpLoggingService.warn("Fraudulent transaction detected: " + transaction);  // 记录欺诈交易日志

                //发布欺诈交易消息到 GCP Pub/Sub
                //messagePublisherService.publishTransaction(transaction);
            }

            return ApiResponse.success(result);
        } catch (Exception e) {
            gcpLoggingService.error("Error while processing transaction: " + transaction, e);
            System.out.println("Error while processing transaction: " + transaction + e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error processing transaction. Please try again later.");
        }
    }
}
