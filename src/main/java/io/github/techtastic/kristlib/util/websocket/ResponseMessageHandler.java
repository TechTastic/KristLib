package io.github.techtastic.kristlib.util.websocket;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.Nullable;

import javax.websocket.MessageHandler;
import java.util.HashMap;

/**
 * This class is for handling responses to WSS requests
 */
public class ResponseMessageHandler implements MessageHandler.Whole<String> {
    private final HashMap<Integer, String> responses = new HashMap<>();

    @Override
    public void onMessage(String response) {
        if (!KristUtil.DECODER.willDecode(response))
            return;

        JsonObject decoded = KristUtil.DECODER.decode(response);
        if (decoded.has("id"))
            responses.put(decoded.get("id").getAsInt(), response);
    }

    @Nullable
    public String getResponseOrNull(int id) {
        return responses.getOrDefault(id, null);
    }

    public void clearResponses() {
        this.responses.clear();
    }
}
