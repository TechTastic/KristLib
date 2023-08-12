package io.github.techtastic.kristlib.websocket;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;
import io.github.techtastic.kristlib.util.KristURLConstants;
import org.jetbrains.annotations.NotNull;

import javax.websocket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class ConnectionManager {
    public int ID = 0;
    public WebSocketContainer container;
    public KristClientEndpoint endpoint;
    public Session session;

    public ConnectionManager() {
        this.container = ContainerProvider.getWebSocketContainer();
        this.endpoint = new KristClientEndpoint();
    }

    public int getIDForUsage() {
        int id = this.ID;
        this.ID++;
        return id;
    }

    public void initConnection() throws URISyntaxException, DeploymentException, IOException {
        JsonObject initResponse = getInitialWebsocketConnection();

        if (initResponse.has("exception"))
            throw new RuntimeException("Failed to Connect to Krist Server!\n" + initResponse.get("exception").getAsString());

        this.session = this.container.connectToServer(this.endpoint, new URI(initResponse.get("url").getAsString()));
        
        System.out.println("Successfully Connected to Krist Server!");
    }

    public JsonObject getInitialWebsocketConnection() {
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
            return Main.DECODER.decode(sb.toString());
        } catch (Exception e) {
            e.fillInStackTrace();

            JsonObject err = new JsonObject();
            err.addProperty("execption", e.toString());
            return err;
        }
    }

    @NotNull
    public JsonObject getInfoFromURL(String urlString, String method) {
        try {
            // Establish Connection and Set Request
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accepts", "application/json");

            // Get Response
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            // TODO: Get Rid of Debug
            System.out.println(sb);

            return Main.DECODER.decode(sb.toString());
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public JsonObject getInfoFromURL(String urlString, String method, Map<String, String> contents) {
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

            return Main.DECODER.decode(sb.toString());
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public JsonObject getInfoFromWSS(JsonObject obj) {
        int id = getIDForUsage();
        obj.addProperty("id", id);

        try {
            // Clear Older Response if Exists
            this.endpoint.response.clearResponse();

            // Send Request
            this.endpoint.sendRequest(Main.ENCODER.encode(obj));

            // Get Response or Wait
            String response = this.endpoint.response.getResponseOrNull();
            while (response == null || !Main.DECODER.willDecode(response) ||
                    !Main.DECODER.decode(response).has("id") ||
                    Main.DECODER.decode(response).get("id").getAsInt() != id) {
                response = this.endpoint.response.getResponseOrNull();
            }

            return Main.DECODER.decode(response);
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

    @NotNull
    public JsonObject login(String privateKey) {
        // Attempt to Authenticate/Upgrade Connection

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "login");
        obj.addProperty("privatekey", privateKey);

        return getInfoFromWSS(obj);
    }

    @NotNull
    public JsonObject logout() {
        // Attempt to Deauthenticate/Downgrade Connection

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "logout");

        return getInfoFromWSS(obj);
    }

    public boolean isGuest() {
        // Is CConnection Authenticated/Upgraded?

        JsonObject obj = this.getInfoFromWSS("me", new JsonObject());
        return obj.get("isGuest").getAsBoolean();
    }
}
