package com.frauddetectservice.mq;

import com.frauddetectservice.logging.GoogleCloudLoggingService;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MQServiceFactory {

    private final GoogleCredentials googleCredentials;
    private final String messageQueueType = "GooglePubSub";
    private final GoogleCloudLoggingService googleCloudLoggingService;
    @Value("${mq.type}")
    private String mqType;

    public MQServiceFactory(GoogleCredentials googleCredentials, GoogleCloudLoggingService googleCloudLoggingService) {
        this.googleCredentials = googleCredentials;
        this.googleCloudLoggingService = googleCloudLoggingService;
    }

    public MQPublisher getPublisher() {
        switch (mqType) {
            case messageQueueType:
                return new MessagePublisherService(googleCredentials, googleCloudLoggingService);
            default:
                googleCloudLoggingService.warn("Unsupported message queue type: " + mqType);
                throw new IllegalArgumentException("Unsupported message queue type: " + mqType);
        }
    }
}
