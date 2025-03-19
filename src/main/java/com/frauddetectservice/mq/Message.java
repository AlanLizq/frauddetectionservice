package com.frauddetectservice.mq;

import java.util.Map;

/**
 * Standardized message structure for publishing and subscribing.
 */
public class Message {
    private String topic;
    private String payload;
    private Map<String, String> metadata;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
