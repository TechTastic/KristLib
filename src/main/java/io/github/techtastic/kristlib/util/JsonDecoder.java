package io.github.techtastic.kristlib.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * This class is a helper class for getting usable JsonObjects from Strings
 *
 * @author TechTastic
 */
public class JsonDecoder implements Decoder.Text<JsonObject> {

    private static final Gson gson = new Gson();

    @Override
    public JsonObject decode(String s) {
        return gson.fromJson(s, JsonObject.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
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
