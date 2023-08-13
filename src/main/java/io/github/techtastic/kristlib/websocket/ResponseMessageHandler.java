package io.github.techtastic.kristlib.websocket;

import javax.websocket.MessageHandler;

/**
 * This class is for handling responses to WSS requests
 */
public class ResponseMessageHandler implements MessageHandler.Whole<String> {
    private String response;

    @Override
    public void onMessage(String response) {
        this.response = response;
        System.err.println(response);
    }

    public String getResponseOrNull() {
        return this.response;
    }

    public void clearResponse() {
        this.response = null;
    }
}
