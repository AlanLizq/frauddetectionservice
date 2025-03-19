package com.frauddetectservice.mq;

/**
 * Interface for message publishing.
 */
public interface MQPublisher {
    void publish(String message);
}
