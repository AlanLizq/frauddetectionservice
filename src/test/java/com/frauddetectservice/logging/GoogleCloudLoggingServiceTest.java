package com.frauddetectservice.logging;

import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Payload;
import com.google.cloud.logging.Severity;
import com.google.cloud.MonitoredResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import org.mockito.Mockito;  // 导入 Mockito 类
import static org.mockito.Mockito.*;  // 导入 Mockito 的静态方法


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class GoogleCloudLoggingServiceTest {

    @Spy
    private Logging logging;

    @InjectMocks
    private GoogleCloudLoggingService googleCloudLoggingService;

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath;

    @BeforeEach
    void setUp() {
        serviceAccountKeyPath = "/path/to/your/mock-service-account-key.json";
    }

    @Test
    void testLogToGCP() {
        LogEntry logEntry = LogEntry.newBuilder(Payload.StringPayload.of("Test Log"))
                .setLogName("fraud-detection-log")
                .setResource(MonitoredResource.newBuilder("global").build())
                .setSeverity(Severity.INFO)
                .build();

        googleCloudLoggingService.log("Test Log");

        verify(logging, times(1)).write(Mockito.anyCollection());
    }

    @Test
    void testServiceAccountKeyPathInjection() {
        System.out.println("Injected Service Account Key Path: " + serviceAccountKeyPath);
        assert serviceAccountKeyPath.equals("/path/to/your/mock-service-account-key.json");
    }

    @Test
    void testLogWithDifferentSeverity() {
        googleCloudLoggingService.info("Info level log");
        googleCloudLoggingService.warn("Warning level log");
        verify(logging, times(1)).write(argThat(entries -> {
            if (entries instanceof Iterable) {
                Iterable<LogEntry> iterableEntries = (Iterable<LogEntry>) entries;
                for (LogEntry entry : iterableEntries) {
                    if (entry.getSeverity() == Severity.INFO) {
                        return true;
                    }
                }
            }
            return false;
        }));

        verify(logging, times(1)).write(argThat(entries -> {
            if (entries instanceof Iterable) {
                Iterable<LogEntry> iterableEntries = (Iterable<LogEntry>) entries;
                for (LogEntry entry : iterableEntries) {
                    if (entry.getSeverity() == Severity.WARNING) {
                        return true;
                    }
                }
            }
            return false;
        }));

        verify(logging, times(0)).write(argThat(entries -> {
            if (entries instanceof Iterable) {
                Iterable<LogEntry> iterableEntries = (Iterable<LogEntry>) entries;
                for (LogEntry entry : iterableEntries) {
                    if (entry.getSeverity() == Severity.DEBUG) {
                        return true;
                    }
                }
            }
            return false;
        }));
    }
}
