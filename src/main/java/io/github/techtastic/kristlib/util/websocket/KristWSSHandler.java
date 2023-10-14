package io.github.techtastic.kristlib.util.websocket;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;

public class KristWSSHandler {
    public int ID = 0;
    public WebSocketContainer container;
    public KristClientEndpoint endpoint;
    public Session session;
    private boolean isConnectionInitialized = false;

    public KristWSSHandler() {
        this.container = ContainerProvider.getWebSocketContainer();
        this.endpoint = new KristClientEndpoint();
    }

    private int getIDForUsage() {
        int id = this.ID;
        this.ID++;
        return id;
    }

    public boolean isInitialized() {
        return this.isConnectionInitialized;
    }

    /**
     * Initializes the connection to the Krist WebSocket
     */
    public void initialize() {
        if (isConnectionInitialized) {
            KristUtil.WSS_LOGGER.log(Level.WARNING, "This connection has already been initialized!");
            return;
        }

        try {
            URL url = new URL(KristURLConstants.KRIST_WEBSOCKET_INITIALIZE.getUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JsonObject sync = KristUtil.DECODER.decode(sb.toString());

            if (sync.has("exception")) {
                KristUtil.WSS_LOGGER.log(Level.SEVERE, "Failed to Connect to Krist Server!\n" + sync.get("exception").getAsString());
                return;
            }

            this.session = this.container.connectToServer(this.endpoint, new URI(sync.get("url").getAsString()));

            this.isConnectionInitialized = true;
            KristUtil.WSS_LOGGER.info("Successfully Connected to Krist Server!");
        } catch (Exception e) {
            KristUtil.WSS_LOGGER.severe("Cannot connect to Krist Server!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the connection to the Krist WebSocket server
     */
    public void disconnect() {
        if (!this.isConnectionInitialized) {
            KristUtil.WSS_LOGGER.severe("Connection to Krist network has not been initialized!");
            throw new RuntimeException();
        }

        try {
            this.session.close();
        } catch (Exception e) {
            KristUtil.WSS_LOGGER.severe("Cannot disconnect from Krist Server!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a request to the Krist WebSocket server and waits for a response
     *
     * @param obj the request sent to the Krist WebSocket
     * @return the response from the Krist WebSocket
     */
    @NotNull
    public JsonObject getInfoFromWSS(JsonObject obj) {
        if (!this.isConnectionInitialized) {
            KristUtil.WSS_LOGGER.severe("Connection to Krist network has not been initialized!");
            throw new RuntimeException();
        }

        int id = getIDForUsage();
        obj.addProperty("id", id);

        try {
            // Clear Older Response if Exists
            this.endpoint.response.clearResponses();

            // Send Request
            this.endpoint.sendRequest(KristUtil.ENCODER.encode(obj));

            // Get Response or Wait
            String response = this.endpoint.response.getResponseOrNull(id);
            while (response == null) {
                response = this.endpoint.response.getResponseOrNull(id);
            }

            return KristUtil.validateResponse(KristUtil.DECODER.decode(response), KristUtil.WSS_LOGGER);
        } catch (Exception e) {
            KristUtil.WSS_LOGGER.severe("Failed to retrieve response from WSS Gateway!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a request to the Krist WebSocket server and waits for a response
     *
     * @param type the type of the request
     * @param obj the request to be sent to the Krist WebSocket
     * @return the response from the Krist WebSocket
     */
    @NotNull
    public JsonObject getInfoFromWSS(WSSRequestType type, JsonObject obj) {
        obj.addProperty("type", type.getType());
        return getInfoFromWSS(obj);
    }

    /**
     * Logs into an authenticated Krist WebSocket connection
     *
     * @return response from Krist WebSocket
     */
    @NotNull
    public JsonObject login(String privateKey) {
        JsonObject obj = new JsonObject();
        obj.addProperty("privatekey", privateKey);

        return getInfoFromWSS(WSSRequestType.LOGIN, obj);
    }

    /**
     * Logs out of an authenticated Krist WebSocket connection if the connection was authenticated
     *
     * @return response from Krist WebSocket
     */
    @NotNull
    public JsonObject logout() {
        JsonObject obj = new JsonObject();
        return getInfoFromWSS(WSSRequestType.LOGOUT, obj);
    }

    /**
     * Checks if the Krist WebSocket connection has been authenticated
     *
     * @return if the WebSocket is authenticated
     */
    @NotNull
    public Boolean isGuest() {
        JsonObject obj = getInfoFromWSS(WSSRequestType.ME, new JsonObject());
        return obj.get("isGuest").getAsBoolean();
    }

    /**
     * Grabs the latest event for the subscribed event levels
     *
     * @return the event or null
     */
    @Nullable
    public JsonObject getLatestEventOrNull() {
        try {
            return KristUtil.DECODER.decode(this.endpoint.event.getEventOrNull());
        } catch (Exception e) {
            return null;
        }
    }
}
