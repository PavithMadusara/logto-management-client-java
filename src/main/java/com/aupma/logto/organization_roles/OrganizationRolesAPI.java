package com.aupma.logto.organization_roles;

import com.aupma.logto.LogtoApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@RequiredArgsConstructor
public class OrganizationRolesAPI {

    public static final String URI = "/organization-roles";
    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;

    public GetOrganizationRoles.Response get() {
        String endpointUrl = baseUrl + URI;
        GetOrganizationRoles endpoint = new GetOrganizationRoles(apiClient, endpointUrl, objectMapper);
        try {
            return endpoint.execute();
        } catch (Exception e) {
            log.error("Error getting organization roles at {}: {}", endpointUrl, e.getMessage(), e);
            throw new LogtoApiException("Failed to get organization roles");
        }
    }
}
