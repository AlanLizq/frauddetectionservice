package com.frauddetectservice.mq;

public interface MQPublisher {
    void publish(String message);
}
