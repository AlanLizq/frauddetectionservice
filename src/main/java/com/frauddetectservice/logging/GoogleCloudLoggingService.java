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
public class GoogleCloudLoggingService implements LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudLoggingService.class); // SLF4J Logger

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath;

    private Logging logging;

    @PostConstruct
    public void init() {
        System.out.println("Service Account Key Path: " + serviceAccountKeyPath);

        if (serviceAccountKeyPath == null || serviceAccountKeyPath.isEmpty()) {
            throw new RuntimeException("Service account key path is not configured properly: " + serviceAccountKeyPath);
        }

        File serviceAccountFile = new File(serviceAccountKeyPath);
        if (!serviceAccountFile.exists()) {
            throw new RuntimeException("Service account key file not found: " + serviceAccountKeyPath);
        }

        try {
            logging = LoggingOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(serviceAccountKeyPath)))
                    .build()
                    .getService();
        } catch (IOException e) {
            logger.error("Failed to initialize google cloud logging service: ", e);
            throw new RuntimeException("Failed to initialize google cloud logging Logging service", e);
        }
    }

    private void logCreateToGCP(String message, Severity severity) {
        try {
            LogEntry entry = LogEntry.newBuilder(Payload.StringPayload.of(message))
                    .setLogName("fraud-detection-log")
                    .setResource(MonitoredResource.newBuilder("global").build())
                    .setSeverity(severity)
                    .build();

            logging.write(Collections.singleton(entry));
            logger.info("Log sent successfully to google cloud logging: {}", severity.name());
        } catch (Exception e) {
            logger.error("Failed to send log to google cloud logging: ", e);
        }
    }

    @Override
    public void log(String message) {
        logCreateToGCP(message, Severity.DEFAULT);
        logger.info(message);
    }

    @Override
    public void info(String message) {
        logCreateToGCP(message, Severity.INFO);
        logger.info(message);
    }

    @Override
    public void debug(String message) {
        logCreateToGCP(message, Severity.DEBUG);
        logger.debug(message);
    }

    @Override
    public void warn(String message) {
        logCreateToGCP(message, Severity.WARNING);
        logger.warn(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        String fullMessage = message + "\n" + throwable.getMessage();
        logCreateToGCP(fullMessage, Severity.ERROR);
        logger.error(message, throwable);
    }
}
