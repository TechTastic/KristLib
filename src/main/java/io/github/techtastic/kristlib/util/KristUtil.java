package io.github.techtastic.kristlib.util;

import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * This class is a helper class for any miscellaneous methods
 *
 * @author TechTastic
 */
public class KristUtil {
    public static String getPrivateKeyFromPassword(String password) {
        return sha256("KRISTWALLET" + password) + "-000";
    }

    public static String sha256(String original) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(original.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static JsonObject validateResponse(JsonObject response) {
        if (!response.get("ok").getAsBoolean())
            throw new RuntimeException(response.get("error").getAsString());

        return response;
    }

    public static JsonObject validateAuth(JsonObject response) {
        validateResponse(response);

        if (!response.get("auth").getAsBoolean())
            throw new RuntimeException(response.get("error").getAsString());

        return response;
    }
}