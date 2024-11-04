package com.aupma.logto.organizations;

import com.aupma.logto.LogtoApiException;
import com.aupma.logto.NotImplementedException;
import com.aupma.logto.organizations.id.OrganizationAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@RequiredArgsConstructor
public class OrganizationsAPI {

    public static final String URI = "/organizations";

    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;

    public CreateOrganization.Response create(CreateOrganization.Request request) {
        String endpointUrl = baseUrl + URI;
        CreateOrganization endpoint = new CreateOrganization(apiClient, endpointUrl, objectMapper);
        try {
            return endpoint.execute(request);
        } catch (Exception e) {
            log.error("Error creating organization at {}: {}", endpointUrl, e.getMessage(), e);
            throw new LogtoApiException("Failed to create organization");
        }
    }

    public void get() {
        throw new NotImplementedException("TODO");
    }

    public OrganizationAPI id(String organizationId) {
        return new OrganizationAPI(baseUrl, apiClient, objectMapper, organizationId);
    }


}
