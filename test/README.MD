# JMeter Performance Test Guide

## 1. Generate HTML Report via Command Line

### Prerequisites
- Install JMeter from [Official Website](https://jmeter.apache.org/)

### Command Syntax
```bash
jmeter -g [JTL_FILE_PATH] -o [OUTPUT_DIRECTORY]
```

| Parameter               | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `[JTL_FILE_PATH]`        | Full path to your .jtl result file (e.g., `/home/user/test-results.jtl`)    |
| `[OUTPUT_DIRECTORY]`     | Empty/non-existing output directory (e.g., `/home/user/report/`)           |

### Example Execution
```bash
jmeter -g /home/user/test-results.jtl -o /home/user/report/
```

### Report Contents
- Response time distribution
- Throughput metrics
- Error rate analysis
- Transaction per second
- Latency percentiles

> **Important**: Output directory must be empty/non-existent. Delete existing directory or choose a new path.

---

## 2. Performance Test Scenarios

### Core Metrics
| Metric               | Description                                  | Typical Values        |
|----------------------|----------------------------------------------|-----------------------|
| Concurrent Users     | Expected system load capacity               | 100, 500, 1000 users |
| Test Duration        | Continuous operation period                 | 10-30 minutes        |

---

### Detailed Test Plans

#### Scenario 1: Baseline Load Test
```properties
Threads: 100
Ramp-Up: 100 seconds
Loops: 10
```
- **Purpose**: Simulate 100 users gradually accessing system over 100 seconds

#### Scenario 2: Incremental Load Test
```properties
Threads: 10
Ramp-Up: 60 seconds
Loops: 100
```
- **Purpose**: Test system behavior under slowly increasing request volume

#### Scenario 3: Stress Test
```properties
Threads: 5000
Ramp-Up: 300 seconds
Loops: ∞ (Forever)
```
- **Purpose**: Identify system breaking point with 5000 concurrent users

#### Scenario 4: Stability Test
```properties
Threads: 100
Ramp-Up: 60 seconds
Loops: ∞ (Forever)
```
- **Purpose**: Monitor long-term performance under sustained load

#### Scenario 5: Capacity Test
```properties
Threads: 200
Ramp-Up: 120 seconds
Loops: 100
```
- **Purpose**: Determine maximum operational capacity