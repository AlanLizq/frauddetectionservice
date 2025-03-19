package com.frauddetectservice.mq;

import com.frauddetectservice.model.Transaction;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * GCP Pub/Sub implementation of MQPublisher and MQSubscriber.
 */
@Service
public class MessagePublisherService implements MQPublisher {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.topic-id}")
    private String topicId;

    private final GoogleCredentials googleCredentials;

    public MessagePublisherService(GoogleCredentials googleCredentials) {
        this.googleCredentials = googleCredentials;
    }

    @Override
    public void publish(String message) {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

            // Initialize Publisher with GoogleCredentials
            Publisher publisher = Publisher.newBuilder(topicName)
                    .setCredentialsProvider(() -> googleCredentials)
                    .build();

            ByteString data = ByteString.copyFrom(message.getBytes());
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

            publisher.publish(pubsubMessage).get(5, TimeUnit.SECONDS);
            System.out.println("Message published to topic " + topicId + ": " + message);

            // Shutdown Publisher to release resources
            publisher.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to publish message: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Publisher: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to publish a Transaction object with attributes.
     * Retained from the original implementation for backward compatibility.
     */
    public void publishTransaction(Transaction transaction) {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);

            // Initialize Publisher with GoogleCredentials
            Publisher publisher = Publisher.newBuilder(topicName)
                    .setCredentialsProvider(() -> googleCredentials)
                    .build();

            ByteString data = ByteString.copyFrom(transaction.getTransactionId().getBytes());
            PubsubMessage message = PubsubMessage.newBuilder()
                    .setData(data)
                    .putAttributes("accountId", transaction.getAccountId())
                    .putAttributes("amount", String.valueOf(transaction.getAmount()))
                    .build();

            publisher.publish(message).get(60, TimeUnit.SECONDS);
            System.out.println("Transaction published: " + transaction.getTransactionId());

            // Shutdown Publisher to release resources
            publisher.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to publish transaction: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Publisher: " + e.getMessage(), e);
        }
    }
}
