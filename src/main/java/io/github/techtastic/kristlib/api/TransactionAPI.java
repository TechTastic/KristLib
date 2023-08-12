package io.github.techtastic.kristlib.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;
import io.github.techtastic.kristlib.api.transaction.Transaction;
import io.github.techtastic.kristlib.util.JsonEncoder;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionAPI {
    public static Transaction makeTransactionWithPassword(String password, String recipient, int amount, JsonObject metadata) {
        return makeTransaction(KristUtil.getPrivateKeyFromPassword(password), recipient, amount, metadata);
    }

    public static Transaction makeTransaction(String privatekey, String recipient, int amount, JsonObject metadata) {
        // Test password/privatekey
        KristUtil.validateResponse(Main.manager.login(privatekey));
        // Test recipient
        KristUtil.validateResponse(Main.sendHTTPRequest(KristURLConstants.KRIST_ADDRESSES + "/" + recipient, "GET"));

        JsonObject request = new JsonObject();
        request.addProperty("privatekey", privatekey);
        request.addProperty("to", recipient);
        request.addProperty("amount", amount);
        try {
            request.addProperty("metadata", Main.ENCODER.encode(metadata));
        } catch (Exception e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }

        return new Transaction(KristUtil.validateResponse(Main.sendWSSRequest(request)));
    }

    public static Transaction getTransaction(int id) {
        return new Transaction(KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_TRANSACTIONS_URL + "/" + id, "GET")));
    }

    public static List<Transaction> getAllTransactions(boolean excludeMined, int limit, int offset) {
        List<Transaction> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_TRANSACTIONS_URL + "?limit=" + limit +
                        "&offset=" + offset + "&excludeMined=" + excludeMined, "GET"));
        JsonArray array = response.getAsJsonArray("transactions");
        for (JsonElement element : array) {
            list.set(array.asList().indexOf(element), new Transaction(element.getAsJsonObject()));
        }
        return list;
    }
}
