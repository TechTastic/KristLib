package io.github.techtastic.kristlib;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.JsonDecoder;
import io.github.techtastic.kristlib.util.JsonEncoder;
import io.github.techtastic.kristlib.websocket.ConnectionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class KristConnectionHandler {

    private static final ConnectionManager manager = new ConnectionManager();
    public static JsonEncoder ENCODER = new JsonEncoder();
    public static JsonDecoder DECODER = new JsonDecoder();

    public static void main(String[] args) {
    }

    public static void startKristServerConnection() {
        manager.initConnection();
    }

    @NotNull
    public static JsonObject sendWSSRequest(JsonObject request) {
        return manager.getInfoFromWSS(request);
    }

    @NotNull
    public static JsonObject sendHTTPRequest(String url, String method) {
        return manager.getInfoFromURL(url, method);
    }

    @NotNull
    public static JsonObject sendHTTPRequestWithContent(String url, String method, Map<String, String> contents) {
        return manager.getInfoFromURL(url, method, contents);
    }

    @NotNull
    public static JsonObject login(String privateKey) {
        // Attempt to Authenticate/Upgrade Connection

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "login");
        obj.addProperty("privatekey", privateKey);

        return manager.getInfoFromWSS(obj);
    }

    @NotNull
    public static JsonObject logout() {
        // Attempt to De-authenticate/Downgrade Connection

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "logout");

        return manager.getInfoFromWSS(obj);
    }

    @NotNull
    public static Boolean isGuest() {
        // Is Connection Authenticated/Upgraded?

        JsonObject obj = manager.getInfoFromWSS("me", new JsonObject());
        return obj.get("isGuest").getAsBoolean();
    }

    @Nullable
    public static JsonObject getLatestEvent() {
        return manager.getLatestEvent();
    }
}