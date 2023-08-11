package io.github.techtastic.kristlib;

import io.github.techtastic.kristlib.util.JsonDecoder;
import io.github.techtastic.kristlib.util.JsonEncoder;
import io.github.techtastic.kristlib.websocket.ConnectionManager;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static String KRIST_SERVER_URL = "krist.dev";
    public static String WEBSOCKET = "wss://";
    public static String HTTPS = "https://";

    public static ConnectionManager manager = new ConnectionManager();
    public static JsonEncoder ENCODER = new JsonEncoder();
    public static JsonDecoder DECODER = new JsonDecoder();

    public static String getWebsocketForKristServer() {
        return WEBSOCKET + KRIST_SERVER_URL;
    }

    public static String getStarterWebsocketforKristServer() {
        return getWebsocketForKristServer() + "/ws/start";
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        startUp();
    }

    public static void startUp() {
        try {
            manager.initConnection();

            //manager.sendMessageToServer();
        } catch (URISyntaxException | DeploymentException | IOException e) {
            System.err.println("Cannot connect to Krist Server!\nError: " + e);
            e.printStackTrace();
        }
    }
}