package io.github.techtastic.kristlib.websocket;

import io.github.techtastic.kristlib.util.ResponseMessageHandler;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;

public class KristClientEndpoint extends Endpoint {
    Session session;
    public ResponseMessageHandler response = new ResponseMessageHandler();

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        this.session.addMessageHandler(this.response);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
