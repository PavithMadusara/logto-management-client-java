package com.aupma.logto.organizations;

import com.aupma.logto.ApiEndpoint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;

public class CreateOrganization extends ApiEndpoint<CreateOrganization.Response> {

    public CreateOrganization(OkHttpClient client, String url, ObjectMapper objectMapper) {
        super(client, url, objectMapper, Response.class);
    }

    public Response execute(Request dto) throws IOException {
        RequestBody body = RequestBody.create(mapper.writeValueAsString(dto), APPLICATION_JSON);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();
        return process(request);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("customData")
        private Map<String, Object> customData;

        @JsonProperty("isMfaRequired")
        private Boolean isMfaRequired;

        @JsonProperty("branding")
        private Branding branding;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Branding {

            @JsonProperty("logoUrl")
            private String logoUrl;

            @JsonProperty("darkLogoUrl")
            private String darkLogoUrl;

            @JsonProperty("favicon")
            private String favicon;

            @JsonProperty("darkFavicon")
            private String darkFavicon;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Response extends Request {
        @JsonProperty("id")
        private String id;

        @JsonProperty("createdAt")
        private Double createdAt;

    }
}
