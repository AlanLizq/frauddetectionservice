package com.frauddetectservice.mq;

/**
 * Interface for message subscribing.
 */
public interface MQSubscriber {
    void subscribe(String topic);
}
