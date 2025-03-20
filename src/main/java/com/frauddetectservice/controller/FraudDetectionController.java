package com.frauddetectservice.controller;

import com.frauddetectservice.model.Transaction;
import com.frauddetectservice.mq.MessagePublisherService;
import com.frauddetectservice.response.ApiResponse;
import com.frauddetectservice.service.DetectionService;
import com.frauddetectservice.logging.GoogleCloudLoggingService;  // 引入 GcpLoggingService
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fraud")
public class FraudDetectionController {

    private final DetectionService detectionService;
    private final GoogleCloudLoggingService googleCloudLoggingService;
    private final MessagePublisherService messagePublisherService;

    // 修改构造函数，注入 GcpLoggingService 和 GCPPubSubService
    public FraudDetectionController(DetectionService detectionService,
                                    GoogleCloudLoggingService googleCloudLoggingService,
                                    MessagePublisherService messagePublisherService) {
        this.detectionService = detectionService;
        this.googleCloudLoggingService = googleCloudLoggingService;
        this.messagePublisherService = messagePublisherService;
    }

    @PostMapping("/detect")
    public ApiResponse<?> detectFraud(@RequestBody Transaction transaction) {
        try {
            googleCloudLoggingService.info("Received transaction for fraud defect: " + transaction);

            String result = detectionService.detectFraud(transaction);

            if (result.startsWith("Fraudulent")) {
                messagePublisherService.publish(transaction.getTransactionId() + " | defect-result: " + result);
                googleCloudLoggingService.warn("Fraud risk transaction detected: " + transaction);
                messagePublisherService.publishTransaction(transaction);
            }

            return ApiResponse.success(result);
        } catch (Exception e) {
            googleCloudLoggingService.error("Error while processing transaction: " + transaction, e);
            System.out.println("Error while processing transaction: " + transaction + e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error processing transaction. Please try again later.");
        }
    }
}
