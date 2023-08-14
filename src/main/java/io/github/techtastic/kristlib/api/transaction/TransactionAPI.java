package io.github.techtastic.kristlib.api.transaction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.KristConnectionHandler;
import io.github.techtastic.kristlib.api.address.KristAddress;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TransactionAPI {
    @NotNull
    public static KristTransaction makeTransaction(String privatekey, String recipient, int amount, String metadata) {
        JsonObject request = new JsonObject();
        request.addProperty("type", "make_transaction");
        request.addProperty("privatekey", privatekey);
        request.addProperty("to", recipient);
        request.addProperty("amount", amount);
        request.addProperty("metadata", metadata);

        KristUtil.validateAuth(KristConnectionHandler.login(privatekey));
        KristTransaction transaction = new KristTransaction(
                KristUtil.sendAndValidateWSSRequest(request));
        KristConnectionHandler.logout();

        return transaction;
    }

    @NotNull
    public static KristTransaction makeTransaction(String privatekey, KristAddress recipient, int amount, String metadata) {
        return makeTransaction(privatekey, recipient.getAddress(), amount, metadata);
    }

    @NotNull
    public static KristTransaction makeTransaction(String privatekey, String recipient, int amount, CommonMeta metadata) {
        return makeTransaction(privatekey, recipient, amount, metadata == null ? null : metadata.getRawMeta());
    }

    @NotNull
    public static KristTransaction makeTransaction(String privatekey, KristAddress recipient, int amount, CommonMeta metadata) {
        return makeTransaction(privatekey, recipient.getAddress(), amount, metadata == null ? null : metadata.getRawMeta());
    }

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
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_TRANSACTIONS_URL + "?limit=" + limit +
                        "&offset=" + offset + "&excludeMined=" + excludeMined, "GET");
        JsonArray transactions = response.getAsJsonArray("transactions");
        for (JsonElement element : transactions) {
            list.add(transactions.asList().indexOf(element),
                    new KristTransaction(element.getAsJsonObject()));
        }
        return list;
    }

    @NotNull
    public static Integer getTotalTransactionAmount(boolean excludeMined) {
        return KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_TRANSACTIONS_URL + "?limit=" + 1 +
                        "&excludeMined=" + excludeMined, "GET").get("total").getAsInt();
    }

    @NotNull
    public static List<KristTransaction> getAllOwnedTransactions(String address, int limit, int offset, boolean excludeMined) {
        List<KristTransaction> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address +
                        "/transactions" + "?limit=" + limit + "&offset=" + offset +
                        "&excludeMined=" + excludeMined, "GET");
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
