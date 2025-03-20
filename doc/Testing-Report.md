# Fraud Detection System Test Plan

---

## 1. Overview

### 1.1 Unit Testing
Unit testing validates individual code units (e.g., methods) using **JUnit 5** and **Mockito**. Key objectives:
- Verify functional correctness
- Isolate dependencies via mocking
- Achieve 80%+ code coverage

### 1.2 Integration Testing
Integration testing validates component interactions using **Spring Boot Test** and **TestRestTemplate**:
- REST API endpoints
- GCP Logging/PubSub integration
- Database connectivity

---

## 2. Unit Test Plan

### 2.1 Tools
| Tool       | Purpose                          |
|------------|----------------------------------|
| JUnit 5    | Test case execution              |
| Mockito    | Mock external dependencies       |
| JaCoCo     | Code coverage analysis           |

### 2.2 Coverage Report
```text
[INFO] -------------------------------------------------------
[INFO]  Coverage Summary:          
[INFO] -------------------------------------------------------
[INFO] Classes: 85% (34/40)        
[INFO] Methods: 78% (256/328)      
[INFO] Lines:   82% (1342/1636)    
[INFO] Branches:65% (432/664)      
```

### 2.3 Sample Test Code
```java
@ExtendWith(MockitoExtension.class)
class FraudDetectionServiceTest {
    @Mock
    private RuleEngine ruleEngine;
    
    @InjectMocks
    private FraudDetectionService service;

    @Test
    void detectFraud_ShouldReturnEmptyRules() {
        when(ruleEngine.getActiveRules()).thenReturn(Collections.emptyList());
        
        Transaction txn = new Transaction("txn1", "acc123", 1000);
        String result = service.detectFraud(txn);
        
        assertEquals("empty rules", result);
    }
}
```

---

## 3. Integration Test Plan

### 3.1 API Test Case
**Endpoint**: `POST /fraud/check`

**JMeter Configuration**:
```json
{
  "protocol": "HTTP",
  "domain": "localhost",
  "port": 8080,
  "path": "/fraud/check",
  "method": "POST",
  "body": {
    "transactionId": "${__UUID()}",
    "accountId": "${__RandomString(6)}",
    "amount": ${__Random(5000,15000)},
    "timestamp": "${__time(yyyy-MM-dd'T'HH:mm:ss)}"
  }
}
```

### 3.2 GCP Log Validation
```json
// logs-example.json
{
  "insertId": "1fddplrf64b2a6",
  "textPayload": "Fraud detected: {\"transactionId\":\"535d8a4a-14c5...",
  "resource": {
    "type": "global",
    "labels": {"project_id": "your-project"}
  },
  "timestamp": "2025-01-29T03:36:57.349Z"
}
```

---

## 4. Performance Testing

### 4.1 Scenarios
| Scenario       | Threads | Ramp-Up | Loops | Duration | Target TPS |
|----------------|---------|---------|-------|----------|------------|
| Baseline       | 200     | 100s    | 10    | 10m      | ≥100       | 
| Stress         | 5000    | 300s    | ∞     | 30m      | N/A        |
| Endurance      | 100     | 60s     | ∞     | 24h      | ≥50        |

### 4.2 Results
**Baseline Test (UserPlan1.jtl)**:
```text
Summary = 20000 requests
Avg RT: 1309 ms | 90% RT: 2409 ms 
Throughput: 88.66/sec | Errors: 0.01%
```

---

## 5. Resilience Testing

### 5.1 Pod Recovery Test
**Procedure**:
```bash
# Delete active pods
kubectl delete pod fraud-detection-{pod1} fraud-detection-{pod2}

# Monitor recovery
watch kubectl get pods
```

**Results**:
| Metric               | Value       |
|----------------------|-------------|
| Pod Termination Time | 2.1 seconds |
| Full Recovery Time   | 9 seconds   |
| Failed Requests      | 0           |

---

## 6. Conclusion

### 6.1 Key Metrics
| Category          | Status | Target  | Actual   |
|--------------------|--------|---------|----------|
| Unit Test Coverage | ✅      | ≥80%    | 82%      |
| API Success Rate   | ✅      | ≥99.9%  | 99.99%   |
| Max TPS            | ⚠️     | ≥200    | 188.66   |
| Failover Time      | ✅      | ≤10s    | 9s       |

### 6.2 Recommendations
1. Optimize database queries causing 12s+ response times
2. Increase thread pool size for Tomcat connector
3. Add circuit breakers for GCP Pub/Sub integration
