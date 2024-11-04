package com.aupma.logto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public abstract class ApiEndpoint<R> {

    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
    protected final OkHttpClient client;
    protected final String url;
    protected final ObjectMapper mapper;
    private final Class<R> responseType;

    protected R process(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseData = response.body().string();
                return mapper.readValue(responseData, responseType);
            } else {
                throw new LogtoApiException(response.message());
            }
        }
    }

}
