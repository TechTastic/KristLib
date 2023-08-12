package io.github.techtastic.kristlib.api.transaction;

import com.google.gson.JsonObject;

public record Transaction(
        int id,
        String from,
        String to,
        int value,
        String time,
        String name,
        String metadata,
        String sent_metaname,
        String sent_name,
        TransactionType type
) {
    public Transaction(JsonObject transaction) {
        this(
                transaction.get("id").getAsInt(),
                transaction.get("from").getAsString(),
                transaction.get("to").getAsString(),
                transaction.get("value").getAsInt(),
                transaction.get("time").getAsString(),
                transaction.get("name").getAsString(),
                transaction.get("metadata").getAsString(),
                transaction.get("sent_metaname").getAsString(),
                transaction.get("sent_name").getAsString(),
                TransactionType.valueOf(transaction.get("type").getAsString())
        );
    }
}
