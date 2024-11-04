package com.aupma.logto;

import com.aupma.logto.organization_roles.OrganizationRolesAPI;
import com.aupma.logto.organizations.OrganizationsAPI;
import com.aupma.logto.users.UsersAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@RequiredArgsConstructor
public class ManagementAPI {

    private final String baseUrl;
    private final OkHttpClient apiClient;
    private final ObjectMapper objectMapper;

    public OrganizationsAPI organizations() {
        return new OrganizationsAPI(baseUrl, apiClient, objectMapper);
    }

    public OrganizationRolesAPI organizationRoles() {
        return new OrganizationRolesAPI(baseUrl, apiClient, objectMapper);
    }

    public UsersAPI users() {
        return new UsersAPI(baseUrl, apiClient, objectMapper);
    }
}
