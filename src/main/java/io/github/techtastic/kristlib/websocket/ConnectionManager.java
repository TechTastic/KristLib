package io.github.techtastic.kristlib.websocket;

import io.github.techtastic.kristlib.Main;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ConnectionManager {
    public WebSocketContainer container;
    public KristClientEndpoint endpoint;
    public Session session;

    public ConnectionManager() {
        this.container = ContainerProvider.getWebSocketContainer();
        this.endpoint = new KristClientEndpoint();
    }

    public void initConnection() throws URISyntaxException, DeploymentException, IOException {
        this.session = this.container.connectToServer(this.endpoint, new URI(Main.getStarterWebsocketforKristServer()));
        this.endpoint.sendMessage("Content-Type: application/x-www-form-urlencoded");

        String response = this.endpoint.response.getResponseOrNull();
        while (response == null) {
            response = this.endpoint.response.getResponseOrNull();
        }
        System.out.println(response);
    }

    public void sendMessageToServer(String message) throws IOException {
        this.endpoint.sendMessage(message);
    }
}
