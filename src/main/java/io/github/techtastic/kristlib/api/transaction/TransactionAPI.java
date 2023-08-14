package io.github.techtastic.kristlib.api.transaction;

import com.google.gson.JsonObject;

public class TransactionAPI {
    public static KristTransaction getTransaction(int id) {
        return new KristTransaction(id);
    }

    public static KristTransaction getTransaction(JsonObject transaction) {
        return new KristTransaction(transaction);
    }
}
