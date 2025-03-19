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

    @Value("${gcp.pubsub.alert-topic-id}")
    private String alertTopicId;

    @Value("${gcp.project-id}")
    private String projectId;

    /**
     * 配置 GoogleCredentials，用于 GCP 服务认证
     *
     * @return GoogleCredentials 实例
     * @throws RuntimeException 如果加载凭据失败
     */
    @Bean
    public GoogleCredentials googleCredentials() {
        try {
            // 使用文件路径加载 GoogleCredentials
            return GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load GoogleCredentials from the provided service account key file.", e);
        }
    }

    /**
     * 示例：配置 Pub/Sub Publisher（如果需要单例 Publisher 实例）
     *
     * @return Publisher 实例
     */
    @Bean
    public Publisher alertPublisher(GoogleCredentials googleCredentials) {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(projectId, alertTopicId);
            return Publisher.newBuilder(topicName)
                    .setCredentialsProvider(() -> googleCredentials)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Pub/Sub Publisher for alertTopicId: " + alertTopicId, e);
        }
    }
}
