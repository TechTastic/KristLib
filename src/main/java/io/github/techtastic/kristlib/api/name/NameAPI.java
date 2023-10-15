package io.github.techtastic.kristlib.api.name;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.api.address.KristAddress;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for working with the KristName class via multiple methods
 *
 * @author TechTastic
 */
public class NameAPI {
    /**
     * This method is for registering a new name for a Krist address
     *
     * @param privatekey the private key of the address to register the name to
     * @param name the selected name to register
     * @return the newly created name as a KristName
     *
     * Registering a new name costs 500KST on the Krist node
     */
    @NotNull
    public static KristName registerNewName(String privatekey, String name) {
        return new KristName(KristHTTPHandler.getInfoFromHTTPWithContent(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "/" + name,
                HTTPRequestType.POST, Map.of("privatekey", privatekey)).getAsJsonObject("name"));
    }

    /**
     * This method is for grabbing the cost of registering a name from the Krist server
     *
     * @return the cost of registering a name
     */
    @NotNull
    public static Integer getNameCost() {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "/cost", HTTPRequestType.GET);
        return response.get("name_cost").getAsInt();
    }

    /**
     * This method is for checking if a name is already in use
     *
     * @param name the name to check for
     * @return if the name is registered or not
     */
    @NotNull
    public static Boolean isNameAvailable(String name) {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "/check/" + name, HTTPRequestType.GET);
        return response.get("available").getAsBoolean();
    }

    /**
     * This method is for grabbing a name as a KristName
     *
     * @param name the name to grab as a String
     * @return the name as a KristName
     */
    @NotNull
    public static KristName getName(String name) {
        return new KristName(name);
    }

    /**
     * This method is for grabbing a name as a KristName
     *
     * @param name the name to grab as a JsonObject
     * @return the name as a KristName
     */
    @NotNull
    public static KristName getName(JsonObject name) {
        return new KristName(name);
    }

    /**
     * This method is for grabbing a list of KristNames from the Krist server
     *
     * @param limit the number of names to grab
     * @param offset the offset from which ot start the list
     * @return a List of KristNames
     */
    @NotNull
    public static List<KristName> getAllNames(int limit, int offset) {
        List<KristName> list = new ArrayList<>(limit);
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "?limit=" + limit +
                        "&offset=" + offset, HTTPRequestType.GET);

        JsonArray addresses = response.getAsJsonArray("names");
        for (JsonElement element : addresses) {
            list.add(addresses.asList().indexOf(element),
                    new KristName(element.getAsJsonObject()));
        }

        return list;
    }

    /**
     * This method is for grabbing the total number of registered names
     *
     * @return the total number of registered names
     */
    @NotNull
    public static Integer getTotalNameCount() {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "?limit=" + 1, HTTPRequestType.GET);
        return response.get("total").getAsInt();
    }

    /**
     * This method is for grabbing a list of KristNames owned by the given address
     *
     * @param address the owner's address
     * @param limit the number of names to grab
     * @param offset the offset from which ot start the list
     * @return a List of KristNames owned by the address
     */
    @NotNull
    public static List<KristName> getAllNamesOwned(String address, int limit, int offset) {
        List<KristName> list = new ArrayList<>(limit);
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_ADDRESSES.getUrl() + "/" + address + "/names" + "?limit=" + limit +
                        "&offset=" + offset, HTTPRequestType.GET);

        JsonArray addresses = response.getAsJsonArray("names");
        for (JsonElement element : addresses) {
            list.add(addresses.asList().indexOf(element),
                    new KristName(element.getAsJsonObject()));
        }

        return list;
    }

    /**
     * This method is for grabbing a list of KristNames owned by the given address
     *
     * @param address the owner's address
     * @param limit the number of names to grab
     * @param offset the offset from which ot start the list
     * @return a List of KristNames owned by the address
     */
    @NotNull
    public static List<KristName> getAllNamesOwned(KristAddress address, int limit, int offset) {
        return getAllNamesOwned(address.getAddress(), limit, offset);
    }

    /**
     * This method is for grabbing the total amount of names owned by the given address
     *
     * @param address the owner's address
     * @return the total number of owned names for the address
     */
    @NotNull
    public static Integer getTotalNamesOwned(String address) {
        return KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_ADDRESSES.getUrl() + "/" + address + "/names" +
                        "?limit=1", HTTPRequestType.GET).get("total").getAsInt();
    }

    /**
     * This method is for grabbing the total amount of names owned by the given address
     *
     * @param address the owner's address
     * @return the total number of owned names for the address
     */
    @NotNull
    public static Integer getTotalNamesOwned(KristAddress address) {
        return getTotalNamesOwned(address.getAddress());
    }

    /**
     * This method is for transferring a registered name to another address
     *
     * @param privatekey the owner's private key
     * @param recipient the recipient address
     * @param name the registered name
     * @return the name under the new owner
     */
    @NotNull
    public static KristName transferName(String privatekey, String recipient, String name) {
        return new KristName(KristHTTPHandler.getInfoFromHTTPWithContent(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "/" + name + "/transfer", HTTPRequestType.POST,
                Map.of("address", recipient, "privatekey", privatekey)).getAsJsonObject("name"));
    }

    /**
     * This method is for transferring a registered name to another address
     *
     * @param privatekey the owner's private key
     * @param recipient the recipient address
     * @param name the registered name
     * @return the name under the new owner
     */
    public static @NotNull KristName transferName(String privatekey, KristAddress recipient, String name) {
        return transferName(privatekey, recipient.getAddress(), name);
    }

    /**
     * This method is for transferring a registered name to another address
     *
     * @param privatekey the owner's private key
     * @param recipient the recipient address
     * @param name the registered name
     * @return the name under the new owner
     */
    public static @NotNull KristName transferName(String privatekey, String recipient, KristName name) {
        return transferName(privatekey, recipient, name.getName());
    }

    /**
     * This method is for transferring a registered name to another address
     *
     * @param privatekey the owner's private key
     * @param recipient the recipient address
     * @param name the registered name
     * @return the name under the new owner
     */
    public static @NotNull KristName transferName(String privatekey, KristAddress recipient, KristName name) {
        return transferName(privatekey, recipient.getAddress(), name.getName());
    }

    /**
     * This method is for adding data to a registered name
     *
     * @param privatekey the owner's private key
     * @param name the registered name
     * @param data the data to be added
     * @return the name with the added data
     */
    @NotNull
    public static KristName addDataToName(String privatekey, String name, String data) {
        Pattern pattern = Pattern.compile("/^[^\s.?#].[^\s]*$/i");
        Matcher match = pattern.matcher(data);
        if (!match.find() && data.length() > 255) {
            KristUtil.HTTP_LOGGER.severe("Invalid Data Entry!\nData: " + data);
            throw new RuntimeException("Invalid Data Entry!");
        }

        return new KristName(KristHTTPHandler.getInfoFromHTTPWithContent(
                KristURLConstants.KRIST_NAMES_URL.getUrl() + "/" + name + "/update",
                HTTPRequestType.POST, Map.of("privatekey", privatekey, "a", data)).getAsJsonObject("name"));
    }

    /**
     * This method is for adding data to a registered name
     *
     * @param privatekey the owner's private key
     * @param name the registered name
     * @param data the data to be added
     * @return the name with the added data
     */
    @NotNull
    public static KristName addDataToName(String privatekey, KristName name, String data) {
        return addDataToName(privatekey, name.getName(), data);
    }
}
