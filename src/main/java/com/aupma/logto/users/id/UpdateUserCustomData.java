package com.aupma.logto.users.id;

import com.aupma.logto.ApiEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import java.io.IOException;

public class UpdateUserCustomData extends ApiEndpoint<Object> {

    public UpdateUserCustomData(
            OkHttpClient client,
            String url,
            ObjectMapper mapper
    ) {
        super(client, url, mapper, Object.class);
    }

    public Object execute(Object customData) throws IOException {
        RequestBody body = RequestBody.create(mapper.writeValueAsString(customData), APPLICATION_JSON);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .patch(body)
                .build();
        return process(request);
    }
}
