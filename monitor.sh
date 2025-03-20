#!/bin/bash

HPA_LOG="hpa-monitor.log"
PODS_LOG="pods-monitor.log"

echo "Starting HPA monitoring..."
kubectl get hpa -w > $HPA_LOG &
HPA_PID=$!

echo "Starting Pods monitoring..."
kubectl get pods -w > $PODS_LOG &
PODS_PID=$!

trap "kill $HPA_PID $PODS_PID" INT TERM

echo "Monitoring HPA and Pods. Press Ctrl+C to stop."
wait
