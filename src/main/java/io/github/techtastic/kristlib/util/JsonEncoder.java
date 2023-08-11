package io.github.techtastic.kristlib.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.Text<JsonObject> {

    private static final Gson gson = new Gson();

    @Override
    public String encode(JsonObject message) throws EncodeException {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
