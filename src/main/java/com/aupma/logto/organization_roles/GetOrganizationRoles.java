package com.aupma.logto.organization_roles;

import com.aupma.logto.ApiEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.List;

public class GetOrganizationRoles extends ApiEndpoint<GetOrganizationRoles.Response> {

    public GetOrganizationRoles(
            OkHttpClient client,
            String url,
            ObjectMapper mapper
    ) {
        super(client, url, mapper, Response.class);
    }

    public Response execute() throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();
        return process(request);
    }

    @Data
    public static class Response {

        private List<OrganizationRole> roles;

        @Data
        public static class OrganizationRole {
            @JsonProperty("tenantId")
            private String tenantId;

            @JsonProperty("id")
            private String id;

            @JsonProperty("name")
            private String name;

            @JsonProperty("description")
            private String description;

            @JsonProperty("type")
            private String type;

            @JsonProperty("scopes")
            private List<Scope> scopes;

            @JsonProperty("resourceScopes")
            private List<ResourceScope> resourceScopes;

            @Data
            public static class Scope {

                @JsonProperty("id")
                private String id;

                @JsonProperty("name")
                private String name;
            }

            @Data
            public static class ResourceScope {

                @JsonProperty("id")
                private String id;

                @JsonProperty("name")
                private String name;

                @JsonProperty("resource")
                private Resource resource;

                @Data
                public static class Resource {

                    @JsonProperty("id")
                    private String id;

                    @JsonProperty("name")
                    private String name;
                }
            }
        }
    }
}
