package com.aupma.logto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LogtoManagementClient {

    private static final Logger log = LoggerFactory.getLogger(LogtoManagementClient.class);

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;
    private final String resource;

    private long tokenExpiry;
    private String accessToken;
    private final OkHttpClient authClient;

    private final String baseUrl;
    private final OkHttpClient apiClient;

    private final ObjectMapper objectMapper;

    private LogtoManagementClient(
            String clientId,
            String clientSecret,
            String tokenUrl,
            String baseUrl,
            String resource
    ) {
        log.debug("Initializing Logto Management Client");
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.resource = resource;
        this.authClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        this.baseUrl = baseUrl;
        this.apiClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.header("Accept", "application/json");
                    builder.header("Authorization", "Bearer " + getAccessToken());
                    return chain.proceed(builder.build());
                })
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String tokenUrl;
        private String baseUrl;
        private String resource;

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder tokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder resource(String resource) {
            this.resource = resource;
            return this;
        }

        public LogtoManagementClient build() {
            return new LogtoManagementClient(clientId, clientSecret, tokenUrl, baseUrl, resource);
        }
    }

    private void authenticate() {
        log.info("Authenticating");
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("resource", resource)
                .add("scope", "all")
                .build();

        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(body)
                .build();

        try (Response response = authClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                accessToken = jsonNode.get("access_token").asText();
                tokenExpiry = System.currentTimeMillis() + (jsonNode.get("expires_in").asLong() * 1000);
                log.info("Logto Management Client Authentication Successful");
            } else {
                throw new IOException("Authentication failed: " + response.message());
            }
        } catch (IOException e) {
            log.error("Error during authentication", e);
        }
    }

    private void refreshAccessTokenIfNeeded() {
        long bufferTime = 30 * 1000L; // 30 seconds buffer
        if (System.currentTimeMillis() >= (tokenExpiry - bufferTime)) {
            log.info("Refreshing access token");
            authenticate();
        }
    }

    private String getAccessToken() {
        refreshAccessTokenIfNeeded();
        return accessToken;
    }

}

