package com.aupma.logto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LogtoClient {

    private final String clientId;
    private final String clientSecret;
    private final String resource;
    private final String baseUrl;
    private final OkHttpClient authClient;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private long tokenExpiry;

    private LogtoClient(String clientId, String clientSecret, String baseUrl, String apiIdentifier) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.resource = apiIdentifier;
        this.baseUrl = baseUrl;
        this.authClient = createAuthClient();
        this.apiClient = createApiClient();
        log.debug("Logto Management Client initialized.");
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private OkHttpClient createAuthClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private OkHttpClient createApiClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request authorizedRequest = originalRequest.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "Bearer " + getAccessToken())
                            .build();
                    return chain.proceed(authorizedRequest);
                })
                .build();
    }

    private String getAccessToken() {
        refreshAccessTokenIfNeeded();
        return accessToken;
    }

    private synchronized void refreshAccessTokenIfNeeded() {
        if (System.currentTimeMillis() >= tokenExpiry - 30000) { // 30 seconds buffer
            authenticate();
        }
    }

    private synchronized void authenticate() {
        log.info("Authenticating with Logto");
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("resource", resource)
                .add("scope", "all")
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + "/oidc/token")
                .post(body)
                .build();

        try (Response response = authClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                parseTokenResponse(response.body().string());
                log.info("Logto Management Client authenticated successfully.");
            } else {
                log.error("Authentication failed: {}", response.message());
            }
        } catch (IOException e) {
            log.error("Error during authentication", e);
        }
    }

    private void parseTokenResponse(String responseBody) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        accessToken = jsonNode.get("access_token").asText();
        tokenExpiry = System.currentTimeMillis() + (jsonNode.get("expires_in").asLong() * 1000);
    }

    @Builder(builderMethodName = "Builder")
    public static LogtoClient newInstance(String clientId, String clientSecret, String baseUrl, String apiIdentifier) {
        return new LogtoClient(clientId, clientSecret, baseUrl, apiIdentifier);
    }

    public ManagementAPI getManagementAPI() {
        return new ManagementAPI(baseUrl, apiClient, objectMapper);
    }
}
