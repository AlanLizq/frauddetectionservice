package com.frauddetectservice.logging;

import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Payload;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.Severity;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Service
public class GcpLoggingService implements DistributedLoggingService {

    private static final Logger logger = LoggerFactory.getLogger(GcpLoggingService.class); // SLF4J Logger

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath; // 从 application.yml 获取服务账户密钥路径

    private Logging logging;

    @PostConstruct
    public void init() {
        // 打印路径以确保注入正确
        System.out.println("Service Account Key Path: " + serviceAccountKeyPath);

        // 检查路径是否为 null 或空
        if (serviceAccountKeyPath == null || serviceAccountKeyPath.isEmpty()) {
            throw new RuntimeException("Service account key path is not configured properly: " + serviceAccountKeyPath);
        }

        // 手动检查路径是否有效
        File serviceAccountFile = new File(serviceAccountKeyPath);
        if (!serviceAccountFile.exists()) {
            throw new RuntimeException("Service account key file not found: " + serviceAccountKeyPath);
        }

        try {
            // 通过服务账户认证，设置凭证路径
            logging = LoggingOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(serviceAccountKeyPath)))
                    .build()
                    .getService();
        } catch (IOException e) {
            logger.error("Failed to initialize GCP Logging service: ", e);
            throw new RuntimeException("Failed to initialize GCP Logging service", e);
        }
    }

    private void logToGCP(String message, Severity severity) {
        try {
            // 创建日志条目
            LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(message))
                    .setLogName("fraud-detection-log")
                    .setResource(MonitoredResource.newBuilder("global").build())
                    .setSeverity(severity)
                    .build();

            // 写入日志到 GCP
            logging.write(Collections.singleton(entry));
            logger.info("Log sent successfully to GCP: {}", severity.name());
        } catch (Exception e) {
            logger.error("Failed to send log to GCP: ", e);
        }
    }

    @Override
    public void log(String message) {
        logToGCP(message, Severity.DEFAULT);
        logger.info(message); // 控制台日志通过 SLF4J
    }

    @Override
    public void info(String message) {
        logToGCP(message, Severity.INFO);
        logger.info(message);
    }

    @Override
    public void debug(String message) {
        logToGCP(message, Severity.DEBUG);
        logger.debug(message);
    }

    @Override
    public void warn(String message) {
        logToGCP(message, Severity.WARNING);
        logger.warn(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        String fullMessage = message + "\n" + throwable.getMessage();
        logToGCP(fullMessage, Severity.ERROR);
        logger.error(message, throwable);
    }
}
