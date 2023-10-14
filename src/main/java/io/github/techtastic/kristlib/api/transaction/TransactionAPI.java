package io.github.techtastic.kristlib.api.transaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.api.address.KristAddress;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import io.github.techtastic.kristlib.util.websocket.KristWSSHandler;
import io.github.techtastic.kristlib.util.websocket.WSSRequestType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionAPI {

    // HTTP Transaction Handling Methods
    @NotNull
    public static KristTransaction makeTransactionOverHTTP(String privatekey, String recipient, int amount, String metadata) {
        JsonObject request = new JsonObject();
        request.addProperty("privatekey", privatekey);
        request.addProperty("to", recipient);
        request.addProperty("amount", amount);
        if (metadata != null)
            request.addProperty("metadata", metadata);

        HashMap<String, String> map = new HashMap<>();
        map.put("privatekey", privatekey);
        map.put("to", recipient);
        map.put("amount", amount + "");
        if (metadata != null)
            map.put("metadata", metadata);

        return new KristTransaction(
                KristHTTPHandler.getInfoFromHTTPWithContent(KristURLConstants.KRIST_TRANSACTIONS_URL, HTTPRequestType.POST, map).getAsJsonObject("transaction"));
    }

    @NotNull
    public static KristTransaction makeTransactionOverHTTP(String privatekey, KristAddress recipient, int amount, String metadata) {
        return makeTransactionOverHTTP(privatekey, recipient.getAddress(), amount, metadata);
    }

    @NotNull
    public static KristTransaction makeTransactionOverHTTP(String privatekey, String recipient, int amount, CommonMeta metadata) {
        return makeTransactionOverHTTP(privatekey, recipient, amount, metadata.getRawMeta());
    }

    @NotNull
    public static KristTransaction makeTransactionOverHTTP(String privatekey, KristAddress recipient, int amount, CommonMeta metadata) {
        return makeTransactionOverHTTP(privatekey, recipient.getAddress(), amount, metadata.getRawMeta());
    }

    // WSS Transaction Handling Methods
    @NotNull
    public static KristTransaction makeTransactionOverWSS(String recipient, int amount, String metadata, KristWSSHandler websocket) {
        JsonObject request = new JsonObject();
        request.addProperty("to", recipient);
        request.addProperty("amount", amount);
        if (metadata != null)
            request.addProperty("metadata", metadata);

        if (websocket.isGuest()) {
            KristUtil.WSS_LOGGER.severe("Websocket was not authenticated and could not complete transaction!\n");
            throw new RuntimeException(request.toString());
        }

        KristTransaction transaction = new KristTransaction(websocket.getInfoFromWSS(WSSRequestType.TRANSACTION, request).getAsJsonObject("transaction"));
        websocket.logout();
        return transaction;
    }

    @NotNull
    public static KristTransaction makeTransactionOverWSS(KristAddress recipient, int amount, String metadata, KristWSSHandler websocket) {
        return makeTransactionOverWSS(recipient.getAddress(), amount, metadata, websocket);
    }

    @NotNull
    public static KristTransaction makeTransactionOverWSS(String recipient, int amount, CommonMeta metadata, KristWSSHandler websocket) {
        return makeTransactionOverWSS(recipient, amount, metadata.getRawMeta(), websocket);
    }

    @NotNull
    public static KristTransaction makeTransactionOverWSS(KristAddress recipient, int amount, CommonMeta metadata, KristWSSHandler websocket) {
        return makeTransactionOverWSS(recipient.getAddress(), amount, metadata.getRawMeta(), websocket);
    }

    // Generic Transaction Handling Methods
    @NotNull
    public static KristTransaction getTransaction(int id) {
        return new KristTransaction(id);
    }

    @NotNull
    public static KristTransaction getTransaction(JsonObject transaction) {
        return new KristTransaction(transaction);
    }

    @NotNull
    public static List<KristTransaction> getAllTransactions(int limit, int offset, boolean excludeMined) {
        List<KristTransaction> list = new ArrayList<>(limit);
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_TRANSACTIONS_URL.getUrl() + "?limit=" + limit +
                        "&offset=" + offset + "&excludeMined=" + excludeMined, HTTPRequestType.GET);
        
        JsonArray transactions = response.getAsJsonArray("transactions");
        for (JsonElement element : transactions) {
            list.add(transactions.asList().indexOf(element),
                    new KristTransaction(element.getAsJsonObject()));
        }
        
        return list;
    }

    @NotNull
    public static Integer getTotalTransactionAmount(boolean excludeMined) {
        return KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_TRANSACTIONS_URL.getUrl() + "?limit=" + 1 +
                        "&excludeMined=" + excludeMined, HTTPRequestType.GET).get("total").getAsInt();
    }

    @NotNull
    public static List<KristTransaction> getAllOwnedTransactions(String address, int limit, int offset, boolean excludeMined) {
        List<KristTransaction> list = new ArrayList<>(limit);
        String url = KristURLConstants.KRIST_ADDRESSES.getUrl() + "/" + address +
                "/transactions" + "?limit=" + limit + "&offset=" + offset + "&excludeMined=" + excludeMined;
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(url, HTTPRequestType.GET);
        
        JsonArray transactions = response.getAsJsonArray("transactions");
        for (JsonElement element : transactions) {
            list.add(transactions.asList().indexOf(element),
                    new KristTransaction(element.getAsJsonObject()));
        }
        
        return list;
    }

    @NotNull
    public static List<KristTransaction> getAllOwnedTransactions(KristAddress address, int limit, int offset, boolean excludeMined) {
        return getAllOwnedTransactions(address.getAddress(), limit, offset, excludeMined);
    }
}
