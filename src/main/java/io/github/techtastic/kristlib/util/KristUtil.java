package io.github.techtastic.kristlib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.logging.Logger;

/**
 * This class is a helper class for any miscellaneous methods
 *
 * @author TechTastic
 */
public class KristUtil {
    public static final Logger WSS_LOGGER = Logger.getLogger("Krist WSS");
    public static final Logger HTTP_LOGGER = Logger.getLogger("Krist HTTP");
    public static JsonEncoder ENCODER = new JsonEncoder();
    public static JsonDecoder DECODER = new JsonDecoder();

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

    public static JsonObject validateResponse(JsonObject response, Logger logger) {
        if (response.get("error") != null) {
            logger.warning("Error in Response from Krist Server:\n" + response);
            throw new RuntimeException(response.get("error").getAsString());
        }
        return response;
    }

    public static String handleJsonNull(JsonElement element) {
        return element instanceof JsonNull ? element.getAsJsonNull().toString() : element.getAsString();
    }
}
