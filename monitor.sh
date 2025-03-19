#!/bin/bash

# 输出文件路径
HPA_LOG="hpa-monitor.log"
PODS_LOG="pods-monitor.log"

# 开始记录日志
echo "Starting HPA monitoring..."
kubectl get hpa -w > $HPA_LOG &
HPA_PID=$!

echo "Starting Pods monitoring..."
kubectl get pods -w > $PODS_LOG &
PODS_PID=$!

# 监听退出信号以停止监控
trap "kill $HPA_PID $PODS_PID" INT TERM

echo "Monitoring HPA and Pods. Press Ctrl+C to stop."
wait
