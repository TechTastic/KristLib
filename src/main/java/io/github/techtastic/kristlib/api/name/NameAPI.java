package io.github.techtastic.kristlib.api.name;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.KristConnectionHandler;
import io.github.techtastic.kristlib.api.address.KristAddress;
import io.github.techtastic.kristlib.api.transaction.KristTransaction;
import io.github.techtastic.kristlib.api.transaction.TransactionAPI;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used for working with the KristName class via multiple methods
 *
 * @author TechTastic
 */
public class NameAPI {
    @NotNull
    public static KristName registerNewName(String privatekey, String name) {
        return new KristName(KristUtil.sendAndValidateHTTPRequestWithContent(
                KristURLConstants.KRIST_NAMES_URL + "/" + name,
                "POST", Map.of("privatekey", privatekey)));
    }

    @NotNull
    public static Integer getNameCost() {
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_NAMES_URL + "/cost", "GET");
        return response.get("name_cost").getAsInt();
    }

    @NotNull
    public static Boolean isNameAvailable(String name) {
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_NAMES_URL + "/check/" + name, "GET");
        return response.get("available").getAsBoolean();
    }

    @NotNull
    public static KristName getName(String name) {
        return new KristName(name);
    }

    @NotNull
    public static KristName getName(JsonObject name) {
        return new KristName(name);
    }

    @NotNull
    public static List<KristName> getAllNames(int limit, int offset) {
        List<KristName> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_NAMES_URL + "?limit=" + limit +
                        "&offset=" + offset, "GET");
        JsonArray addresses = response.getAsJsonArray("names");
        for (JsonElement element : addresses) {
            list.add(addresses.asList().indexOf(element),
                    new KristName(element.getAsJsonObject()));
        }
        return list;
    }

    @NotNull
    public static Integer getTotalNameCount() {
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_NAMES_URL + "?limit=" + 1, "GET");
        return response.get("total").getAsInt();
    }

    @NotNull
    public static List<KristName> getAllNamesOwned(String address, int limit, int offset) {
        List<KristName> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address + "/names" + "?limit=" + limit +
                        "&offset=" + offset, "GET");
        JsonArray addresses = response.getAsJsonArray("names");
        for (JsonElement element : addresses) {
            list.add(addresses.asList().indexOf(element),
                    new KristName(element.getAsJsonObject()));
        }
        return list;
    }

    @NotNull
    public static List<KristName> getAllNamesOwned(KristAddress address, int limit, int offset) {
        return getAllNamesOwned(address.getAddress(), limit, offset);
    }

    @NotNull
    public static Integer getTotalNamesOwned(String address) {
        return KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address + "/names" +
                        "?limit=1", "GET").get("total").getAsInt();
    }

    @NotNull
    public static Integer getTotalNamesOwned(KristAddress address) {
        return getTotalNamesOwned(address.getAddress());
    }

    @NotNull
    public static KristTransaction transferName(String privatekey, String recipient, String name) {
        return TransactionAPI.getTransaction(KristUtil.sendAndValidateHTTPRequestWithContent(
                KristURLConstants.KRIST_NAMES_URL + "/" + name + "/transfer", "POST",
                Map.of("address", recipient, "privatekey", privatekey)));
    }

    @NotNull
    public static KristTransaction transferName(String privatekey, KristAddress recipient, String name) {
        return transferName(privatekey, recipient.getAddress(), name);
    }

    @NotNull
    public static KristTransaction transferName(String privatekey, String recipient, KristName name) {
        return transferName(privatekey, recipient, name.getName());
    }

    @NotNull
    public static KristTransaction transferName(String privatekey, KristAddress recipient, KristName name) {
        return transferName(privatekey, recipient.getAddress(), name.getName());
    }

    @NotNull
    public static KristName addDataToName(String privatekey, String name, String data) {
        return new KristName(KristUtil.sendAndValidateHTTPRequestWithContent(
                KristURLConstants.KRIST_NAMES_URL + "/" + name, "PUT", Map.of("a", data)));
    }

    @NotNull
    public static KristName addDataToName(String privatekey, KristName name, String data) {
        return addDataToName(privatekey, name.getName(), data);
    }
}
