#!/bin/bash

start_time=$(date +%s)

kubectl delete pod -l app=fraud-detection

while true; do
    pod_status=$(kubectl get pods -l app=fraud-detection --field-selector=status.phase=Running -o jsonpath='{.items[*].status.phase}')
    if [[ "$pod_status" == *"Running"* ]]; then
        break
    fi
    sleep 1
done

end_time=$(date +%s)

recovery_time=$((end_time - start_time))

echo "Pods recovery cost: $recovery_time seconds"
