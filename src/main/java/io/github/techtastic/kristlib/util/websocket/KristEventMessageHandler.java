package io.github.techtastic.kristlib.util.websocket;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.Nullable;

import javax.websocket.MessageHandler;

/**
 * This class is for handling events from the Krist node
 */
public class KristEventMessageHandler implements MessageHandler.Whole<String> {
    private String event;

    @Override
    public void onMessage(String event) {
        if (!verifyEvent(event))
            return;

        this.event = event;
    }

    public boolean verifyEvent(String event) {
        JsonObject eventObject = KristUtil.DECODER.decode(event);
        return eventObject.has("type") &&
                eventObject.get("type").getAsString().equals("event");
    }

    @Nullable
    public String getEventOrNull() {
        return this.event;
    }

    public void clearEvent() {
        this.event = null;
    }
}
