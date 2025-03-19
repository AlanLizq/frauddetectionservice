package com.frauddetectservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GCPPubSubConfigTest {
    @Autowired
    private GCPPubSubConfig gcpPubSubConfig;

    @Test
    void testGoogleCredentials() throws IOException {
        GoogleCredentials credentials = gcpPubSubConfig.googleCredentials();
        assertNotNull(credentials, "GoogleCredentials should not be null");
    }
}
