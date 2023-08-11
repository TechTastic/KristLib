package io.github.techtastic.kristlib.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;

import javax.websocket.DecodeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KristAccount {
    private JsonObject account;
    private List<Transaction> log;

    public KristAccount(String address) throws IOException, DecodeException {
        JsonObject output = Main.manager.getInfoFromURL("https://krist.dev/addresses/" + address + "?fetchNames=true", "GET");
        if (!output.get("ok").getAsBoolean())
            throw new RuntimeException(output.get("message").getAsString());
        this.account = output.get("address").getAsJsonObject();
    }

    public String getAddress() {
        return this.account.get("address").getAsString();
    }

    public int getBalance() {
        return this.account.get("balance").getAsInt();
    }

    public int getTotalIncome() {
        return this.account.get("totalin").getAsInt();
    }

    public int getTotalSpent() {
        return this.account.get("totalout").getAsInt();
    }

    public String getFirstSeen() {
        return this.account.get("firstseen").toString();
    }

    public int getNamesCount() {
        return this.account.get("names").getAsInt();
    }

    public List<Transaction> getTransactionLog(String orderBy, String order, boolean includeMining) throws IOException, DecodeException {
        if (this.log == null || this.log.isEmpty() || this.log.size() != getTotalTransactions())
            this.log = getAllTransactions(orderBy, order, includeMining);
        return this.log;
    }

    public List<Transaction> getTransactionLog() throws IOException, DecodeException {
        if (this.log == null || this.log.isEmpty() || this.log.size() != getTotalTransactions())
            this.log = getAllTransactions("time", "ASC", true);
        return this.log;
    }

    public int getTotalTransactions() throws IOException, DecodeException {
        JsonObject output = Main.manager.getInfoFromURL("https://krist.dev/lookup/transactions/" + getAddress(), "GET");
        return output.get("total").getAsInt();
    }

    private List<Transaction> getAllTransactions(String orderBy, String order, boolean includeMining) throws IOException, DecodeException {
        List<Transaction> list = new ArrayList<>();

        getAllTransactionsRecursion(list, "https://krist.dev/lookup/transactions/" + getAddress() + "?orderBy=" + orderBy + "&order=" + order + "&includeMined=" + includeMining, 0, 0);

        return list;
    }

    // Recursively Loops through all Transactions
    private List<Transaction> getAllTransactionsRecursion(List<Transaction> original, String url, int count, int offset) throws IOException, DecodeException {
        if (count == -1)
            return original;

        String newUrl = url + "&limit=1000";
        if (count == 1000 || offset == 0)
            newUrl = newUrl + "&offset=1000";

        JsonObject output = Main.manager.getInfoFromURL(newUrl, "GET");

        int newCount = output.get("count").getAsInt();

        JsonArray transactions = output.getAsJsonArray("transactions");
        transactions.forEach(element -> {
            JsonObject transactionObject = element.getAsJsonObject().get(String.valueOf(transactions.asList().indexOf(element))).getAsJsonObject();
            Transaction transaction = new Transaction(
                    transactionObject.get("id").getAsInt(),
                    transactionObject.get("from").getAsString(),
                    transactionObject.get("to").getAsString(),
                    transactionObject.get("value").getAsInt(),
                    transactionObject.get("time").getAsString(),
                    transactionObject.get("name").getAsString(),
                    transactionObject.get("metadata").getAsString(),
                    transactionObject.get("sent_metadata").getAsString(),
                    transactionObject.get("sent_name").getAsString(),
                    TransactionType.valueOf(transactionObject.get("type").getAsString())
            );
            original.add(transaction);
        });

        return getAllTransactionsRecursion(original, url, newCount < 1000 ? -1 : newCount, offset + 1000);
    }

    public void updateAll() throws IOException, DecodeException {
        JsonObject output = Main.manager.getInfoFromURL("https://krist.dev/addresses/" + getAddress() + "?fetchNames=true", "GET");
        if (!output.get("ok").getAsBoolean())
            throw new RuntimeException(output.get("message").getAsString());
        this.account = output.get("address").getAsJsonObject();

        getTransactionLog();
    }
}
