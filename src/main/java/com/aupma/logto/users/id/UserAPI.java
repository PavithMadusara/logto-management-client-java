package com.aupma.logto.users.id;

import com.aupma.logto.LogtoApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@RequiredArgsConstructor
public class UserAPI {

    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;
    private final String userId;

    public Object updateCustomData(Object dto) {
        String endpointUrl = baseUrl + "/" + userId + "/custom-data";
        UpdateUserCustomData endpoint = new UpdateUserCustomData(apiClient, endpointUrl, objectMapper);
        try {
            return endpoint.execute(dto);
        } catch (Exception e) {
            log.error("Error updating user custom data at {}: {}", endpointUrl, e.getMessage(), e);
            throw new LogtoApiException("Failed to update user custom data");
        }
    }
}
