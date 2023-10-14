package io.github.techtastic.kristlib.util.websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;

class KristClientEndpoint extends Endpoint {
    Session session;
    public ResponseMessageHandler response = new ResponseMessageHandler();
    public KristEventMessageHandler event = new KristEventMessageHandler();

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;

        this.session.addMessageHandler(response);
        this.session.addMessageHandler(event);
    }

    public void sendRequest(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
