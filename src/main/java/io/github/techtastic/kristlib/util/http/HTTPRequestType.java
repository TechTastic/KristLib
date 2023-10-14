package io.github.techtastic.kristlib.util.http;

public enum HTTPRequestType {
    POST("POST"),
    GET("GET"),
    PUT("PUT");

    final String type;

    HTTPRequestType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
