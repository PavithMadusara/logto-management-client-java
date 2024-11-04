package com.aupma.logto.organizations.id.users;

import com.aupma.logto.LogtoApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class OrganizationUsersAPI {
    public static final String URI = "/users";

    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;

    public void add(List<String> userIds) {
        String endpointUrl = baseUrl + URI;
        AddUsersToOrganization endpoint = new AddUsersToOrganization(apiClient, endpointUrl, objectMapper);
        try {
            endpoint.execute(userIds);
        } catch (Exception e) {
            log.error("Error adding users to organization at {}: {}", endpointUrl, e.getMessage(), e);
            throw new LogtoApiException("Failed to add users to organization");
        }
    }

    public void replace() {
        throw new LogtoApiException("TODO");
    }

    public void get() {
        throw new LogtoApiException("TODO");
    }

    public void assignRoles(List<String> userIds, List<String> organizationRoleIds) {
        String endpointUrl = baseUrl + URI + "/roles";
        AssignRolesToOrganizationUsers endpoint = new AssignRolesToOrganizationUsers(
                apiClient,
                endpointUrl,
                objectMapper
        );

        try {
            endpoint.execute(userIds, organizationRoleIds);
        } catch (Exception e) {
            log.error("Error assigning roles to organization users at {}: {}", endpointUrl, e.getMessage(), e);
            throw new LogtoApiException("Failed to assign roles to organization users");
        }
    }
}
