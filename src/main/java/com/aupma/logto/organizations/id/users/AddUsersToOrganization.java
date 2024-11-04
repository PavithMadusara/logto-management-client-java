package com.aupma.logto.organizations.id.users;

import com.aupma.logto.ApiEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AddUsersToOrganization extends ApiEndpoint<Void> {

    public AddUsersToOrganization(
            OkHttpClient client,
            String url,
            ObjectMapper mapper
    ) {
        super(client, url, mapper, Void.class);
    }

    public void execute(List<String> userIds) throws IOException {
        Map<String, List<String>> dto = Map.of("userIds", userIds);
        RequestBody body = RequestBody.create(mapper.writeValueAsString(dto), APPLICATION_JSON);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();
        process(request);
    }
}
