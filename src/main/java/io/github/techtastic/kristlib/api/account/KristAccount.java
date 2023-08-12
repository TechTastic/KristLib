package io.github.techtastic.kristlib.api.account;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;
import io.github.techtastic.kristlib.api.name.Name;
import io.github.techtastic.kristlib.api.transaction.Transaction;
import io.github.techtastic.kristlib.api.sorting.Order;
import io.github.techtastic.kristlib.api.sorting.OrderBy;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;

import java.util.ArrayList;
import java.util.List;

public record KristAccount(
        String address,
        int balance,
        int totalIncome,
        int totalSpent,
        String firstSeen,
        List<Name> names,
        List<Transaction> log
) {

    public KristAccount(String address) {
        this(
                address,
                getAccountObject(address).get("balance").getAsInt(),
                getAccountObject(address).get("totalin").getAsInt(),
                getAccountObject(address).get("totalout").getAsInt(),
                getAccountObject(address).get("firstseen").toString(),
                getAllNames(address),
                getTransactionLog(address, OrderBy.ID, Order.ASC, true)
        );
    }

    public KristAccount(JsonObject account) {
        this(
                account.get("address").getAsString(),
                account.get("balance").getAsInt(),
                account.get("totalin").getAsInt(),
                account.get("totalout").getAsInt(),
                account.get("firstseen").getAsString(),
                getAllNames(account.get("address").getAsString()),
                getTransactionLog(account.get("address").getAsString(), OrderBy.ID, Order.ASC, true)
        );
    }

    public static JsonObject getAccountObject(String address) {
        return Main.manager.getInfoFromURL(
                KristURLConstants.KRIST_ADDRESSES + "/" + address + "?fetchNames=true", "GET")
                .get("address").getAsJsonObject();
    }

    public static List<Transaction> getTransactionLog(String address, OrderBy orderBy, Order order, boolean includeMining) {
        return getTransactionLog(address, orderBy.toString(), order.toString(), includeMining);
    }

    // Loop through and get all Transactions associated with address
    private static List<Transaction> getTransactionLog(String address, String orderBy, String order, boolean includeMining) {
        int transactionAmount = getTotalTransactions(address);
        List<Transaction> list = new ArrayList<>(transactionAmount);
        for (int offset = 0; offset < transactionAmount; offset++) {
            JsonObject query = KristUtil.validateResponse(Main.sendHTTPRequest(
                    "https://krist.dev/lookup/transactions/" + address +
                            "?orderBy=" + orderBy + "&order=" + order + "&includeMined=" + includeMining +
                            "&limit=1&offset=" + offset, "GET"));
            JsonObject transaction = query.getAsJsonArray("transactions").get(0).getAsJsonObject();
            list.set(offset, new Transaction(transaction));
        }
        return list;
    }

    public static int getTotalTransactions(String address) {
        return Main.manager.getInfoFromURL(
                KristURLConstants.KRIST_TRANSACTIONS_URL + "/" + address, "GET")
                .get("total").getAsInt();
    }

    // Loop thorugh and get all Names associated with address
    public static List<Name> getAllNames(String address) {
        int names = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address +
                        "/names?limit=1", "GET")).get("total").getAsInt();
        List<Name> list = new ArrayList<>(names);
        for (int offset = 0; offset < names; offset++) {
            JsonObject query = KristUtil.validateResponse(Main.sendHTTPRequest(
                    KristURLConstants.KRIST_ADDRESSES + "/" + address +
                            "/names?limit=1&offset=" + offset, "GET"));
            JsonObject name = query.getAsJsonArray("names").get(0).getAsJsonObject();
            list.add(offset, new Name(name));
        }
        return list;
    }

    public boolean isOutdated() {
        return !this.equals(new KristAccount(this.address));
    }
}
