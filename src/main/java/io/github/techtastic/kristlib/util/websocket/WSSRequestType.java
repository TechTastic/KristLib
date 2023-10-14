package io.github.techtastic.kristlib.util.websocket;

public enum WSSRequestType {
    WORK("work"),
    TRANSACTION("make_transaction"),
    VALID_LEVELS("get_valid_subscription_levels"),
    ADDRESS("address"),
    ME("me"),
    CURRENT_LEVELS("get_subscription_level"),
    LOGOUT("logout"),
    LOGIN("login"),
    SUBSCRIBE("subscribe");

    final String type;

    WSSRequestType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
