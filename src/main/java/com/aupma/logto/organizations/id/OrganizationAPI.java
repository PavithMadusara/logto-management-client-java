package com.aupma.logto.organizations.id;

import com.aupma.logto.NotImplementedException;
import com.aupma.logto.organizations.id.users.OrganizationUsersAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@RequiredArgsConstructor
public class OrganizationAPI {
    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;
    private final String organizationId;

    public OrganizationUsersAPI users() {
        return new OrganizationUsersAPI(
                String.join("/", baseUrl, organizationId),
                apiClient,
                objectMapper
        );
    }

    public void get() {
        throw new NotImplementedException("TODO");
    }

    public void update() {
        throw new NotImplementedException("TODO");
    }

    public void delete() {
        throw new NotImplementedException("TODO");
    }

}
