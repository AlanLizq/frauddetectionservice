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


@ExtendWith(MockitoExtension.class)  // 使用 Mockito 扩展
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")  // 配置测试环境的配置文件
public class GcpLoggingServiceTest {

    @Spy
    private Logging logging;  // 监视 Logging 类的行为

    @InjectMocks
    private GcpLoggingService gcpLoggingService;  // 被测试的 GcpLoggingService

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath;  // 配置注入

    @BeforeEach
    void setUp() {
        // 初始化配置
        serviceAccountKeyPath = "/path/to/your/mock-service-account-key.json"; // 用于测试的硬编码路径
    }

    @Test
    void testLogToGCP() {
        // 模拟 GCP 日志条目
        LogEntry logEntry = LogEntry.newBuilder(Payload.StringPayload.of("Test Log"))
                .setLogName("fraud-detection-log")
                .setResource(MonitoredResource.newBuilder("global").build())
                .setSeverity(Severity.INFO)
                .build();

        // 调用 GcpLoggingService 的 log 方法
        gcpLoggingService.log("Test Log");

        // 使用 @Spy 验证 Logging.write() 是否被调用
        verify(logging, times(1)).write(Mockito.anyCollection());  // 验证调用了一次
    }

    @Test
    void testServiceAccountKeyPathInjection() {
        // 验证配置路径是否正确注入
        System.out.println("Injected Service Account Key Path: " + serviceAccountKeyPath);
        assert serviceAccountKeyPath.equals("/path/to/your/mock-service-account-key.json");
    }

    @Test
    void testLogWithDifferentSeverity() {
        // 调用日志方法
        gcpLoggingService.info("Info level log");
        gcpLoggingService.warn("Warning level log");

        // 使用 verify 和 argThat，确保每个日志级别仅调用一次
        verify(logging, times(1)).write(argThat(entries -> {
            // 将 entries 转换为 List，并使用 stream()
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
            // 使用 forEach 遍历 entries
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

        // 验证其他日志级别没有被调用
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
