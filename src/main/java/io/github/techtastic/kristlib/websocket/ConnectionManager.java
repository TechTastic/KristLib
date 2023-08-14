package io.github.techtastic.kristlib.websocket;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.KristConnectionHandler;
import io.github.techtastic.kristlib.util.KristURLConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ConnectionManager {
    public int ID = 0;
    public WebSocketContainer container;
    public KristClientEndpoint endpoint;
    public Session session;
    private boolean isConnectionInitialized = false;

    public ConnectionManager() {
        this.container = ContainerProvider.getWebSocketContainer();
        this.endpoint = new KristClientEndpoint();
    }

    public int getIDForUsage() {
        int id = this.ID;
        this.ID++;
        return id;
    }

    public void initConnection() {
        JsonObject initResponse = getInitialWebsocketConnection();

        if (initResponse.has("exception"))
            throw new RuntimeException("Failed to Connect to Krist Server!\n" + initResponse.get("exception").getAsString());

        try {
            this.session = this.container.connectToServer(this.endpoint, new URI(initResponse.get("url").getAsString()));

            this.isConnectionInitialized = true;
            System.out.println("Successfully Connected to Krist Server!");
        } catch (Exception e) {
            System.out.println("Could not connect to KristServer!");
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private JsonObject getInitialWebsocketConnection() {
        try {
            URL url = new URL(KristURLConstants.KRIST_WEBSOCKET_INITIALIZE);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return KristConnectionHandler.DECODER.decode(sb.toString());
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public JsonObject getInfoFromURL(String urlString, String method) {
        return getInfoFromURL(urlString, method, Map.of());
    }

    @NotNull
    public JsonObject getInfoFromURL(String urlString, String method, Map<String, String> contents) {
        if (!this.isConnectionInitialized)
            throw new RuntimeException("Connection to Krist network has not been initialized!");

        try {
            // Establish Connection and Set Request
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accepts", "application/json");
            contents.forEach(con::setRequestProperty);

            // Get Response
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            con.disconnect();

            // TODO: Get Rid of Debug
            System.out.println(sb);

            return KristConnectionHandler.DECODER.decode(sb.toString());
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public JsonObject getInfoFromWSS(JsonObject obj) {
        if (!this.isConnectionInitialized)
            throw new RuntimeException("Connection to Krist network has not been initialized!");

        int id = getIDForUsage();
        obj.addProperty("id", id);

        try {
            // Clear Older Response if Exists
            this.endpoint.response.clearResponse();

            // Send Request
            this.endpoint.sendRequest(KristConnectionHandler.ENCODER.encode(obj));

            // Get Response or Wait
            String response = this.endpoint.response.getResponseOrNull();
            while (response == null || !KristConnectionHandler.DECODER.willDecode(response) ||
                    !KristConnectionHandler.DECODER.decode(response).has("id") ||
                    KristConnectionHandler.DECODER.decode(response).get("id").getAsInt() != id) {
                response = this.endpoint.response.getResponseOrNull();
            }

            return KristConnectionHandler.DECODER.decode(response);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public JsonObject getInfoFromWSS(String type, JsonObject obj) {
        obj.addProperty("type", type);
        return getInfoFromWSS(obj);
    }

    @Nullable
    public JsonObject getLatestEvent() {
        try {
            return KristConnectionHandler.DECODER.decode(this.endpoint.event.getEventOrNull());
        } catch (Exception e) {
            return null;
        }
    }
}
