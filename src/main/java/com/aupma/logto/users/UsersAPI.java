package com.aupma.logto.users;

import com.aupma.logto.users.id.UserAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@RequiredArgsConstructor
public class UsersAPI {

    public static final String URI = "/users";

    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;

    public UserAPI id(String userId) {
        return new UserAPI(baseUrl, apiClient, objectMapper, userId);
    }
}
