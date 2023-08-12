package io.github.techtastic.kristlib;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.JsonDecoder;
import io.github.techtastic.kristlib.util.JsonEncoder;
import io.github.techtastic.kristlib.websocket.ConnectionManager;

import javax.websocket.DecodeException;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class Main {

    public static ConnectionManager manager = new ConnectionManager();
    public static JsonEncoder ENCODER = new JsonEncoder();
    public static JsonDecoder DECODER = new JsonDecoder();

    public static void main(String[] args) {
        startUp();
    }

    public static void startUp() {
        System.out.println("Connecting to Krist Server...");
        try {
            manager.initConnection();
        } catch (Exception e) {
            System.err.println("Cannot connect to Krist Server!\nError: " + e);
            e.fillInStackTrace();
        }
    }

    public static JsonObject sendWSSRequest(JsonObject request) {
        return manager.getInfoFromWSS(request);
    }

    public static JsonObject sendHTTPRequest(String url, String method) {
        return manager.getInfoFromURL(url, method);
    }

    public static JsonObject sendHTTPRequestWithContent(String url, String method, Map<String, String> contents) {
        return manager.getInfoFromURL(url, method, contents);
    }
}