package com.frauddetectservice.mq;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory class to provide appropriate MQ service based on configuration.
 */
@Component
public class MQServiceFactory {

    private final GoogleCredentials googleCredentials;

    @Value("${mq.type}")
    private String mqType;

    public MQServiceFactory(GoogleCredentials googleCredentials) {
        this.googleCredentials = googleCredentials;
    }

    public MQPublisher getPublisher() {
        switch (mqType) {
            case "GCP":
                return new MessagePublisherService(googleCredentials); // Pass credentials here
            default:
                throw new IllegalArgumentException("Unsupported MQ type: " + mqType);
        }
    }
}
