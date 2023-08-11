package io.github.techtastic.kristlib.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;

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
    public WebSocketContainer container;
    public KristClientEndpoint endpoint;
    public Session session;

    public ConnectionManager() {
        this.container = ContainerProvider.getWebSocketContainer();
        this.endpoint = new KristClientEndpoint();
    }

    public void initConnection() throws URISyntaxException, DeploymentException, IOException, DecodeException, EncodeException {
        JsonObject initResponse = getInitialWebsocketConnection();

        this.session = this.container.connectToServer(this.endpoint, new URI(initResponse.get("url").getAsString()));

        JsonObject obj = new JsonObject();
        obj.addProperty("id", 1);
        obj.addProperty("type", "get_subscription_level");

        System.out.println(getInfoFromWSS(obj, 1));
    }

    public JsonObject getInitialWebsocketConnection() throws DecodeException, IOException {
        URL url = new URL("https://krist.dev/ws/start");
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
    }

    public JsonObject getInfoFromURL(String urlString, String method) throws IOException, DecodeException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accepts", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        System.out.println(sb);

        return Main.DECODER.decode(sb.toString());
    }

    public JsonObject getInfoFromWSS(JsonObject obj, int id) throws IOException, EncodeException, DecodeException {
        this.endpoint.response.clearResponse();

        this.endpoint.sendRequest(Main.ENCODER.encode(obj));

        String response = this.endpoint.response.getResponseOrNull();
        while (response == null || !Main.DECODER.willDecode(response) ||
                !Main.DECODER.decode(response).has("id") ||
                Main.DECODER.decode(response).get("id").getAsInt() != id) {
            response = this.endpoint.response.getResponseOrNull();
        }

        return Main.DECODER.decode(response);
    }

    public JsonObject getInfoFromWSS(int id, String type, JsonObject obj) throws EncodeException, IOException, DecodeException {
        obj.addProperty("id", id);
        obj.addProperty("type", type);
        return getInfoFromWSS(obj, id);
    }

    public JsonObject getInfoFromWSS(Map<String, Object> map) throws EncodeException, IOException, DecodeException {
        JsonObject obj = new JsonObject();
        map.forEach((string, object) -> {
            if (object instanceof JsonElement element)
                obj.add(string, element);
            else if (object instanceof Number num)
                obj.addProperty(string, num);
            else if (object instanceof String str)
                obj.addProperty(string, str);
            else if (object instanceof Boolean bool)
                obj.addProperty(string, bool);
            else if (object instanceof Character ch)
                obj.addProperty(string, ch);
        });

        return getInfoFromWSS(obj, (int) map.getOrDefault("id", 1000));
    }

    public JsonObject login(int id, String privateKey) throws EncodeException, IOException, DecodeException {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("type", "login");
        obj.addProperty("privatekey", privateKey);

        return getInfoFromWSS(obj, id);
    }

    public JsonObject logout(int id) throws EncodeException, IOException, DecodeException {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("type", "logout");

        return getInfoFromWSS(obj, id);
    }
}
