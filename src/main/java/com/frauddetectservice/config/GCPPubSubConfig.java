package com.frauddetectservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GCPPubSubConfig {

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath;

    @Value("${gcp.project-id}")
    private String projectId;

    @Bean
    public GoogleCredentials googleCredentials() {
        try {
            return GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GoogleCredentials from the provided service account key file.", e);
        }
    }
}
