package io.github.techtastic.kristlib.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.Main;
import io.github.techtastic.kristlib.api.account.KristAccount;
import io.github.techtastic.kristlib.api.name.Name;
import io.github.techtastic.kristlib.api.transaction.Transaction;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.glassfish.grizzly.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressAPI {
    private static List<KristAccount> allAccounts;

    public static JsonObject getAccountAsJson(String address) {
        return KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + address + "?fetchNames=true", "GET"));
    }

    public static KristAccount getAccount(String address) {
        return new KristAccount(address);
    }

    public static List<KristAccount> getAllAccounts() {
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "?limit=1", "GET"));
        int totalAccounts = response.get("total").getAsInt();
        if (allAccounts == null || allAccounts.size() != totalAccounts)
            allAccounts = getAllAccountsLoop(totalAccounts);

        updateAllAccounts();

        return allAccounts;
    }

    public static List<KristAccount> getRichestAccounts(int limit, int offset) {
        List<KristAccount> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_RICHEST + "?limit=" + limit +
                        "&offset=" + offset, "GET"));
        JsonArray array = response.getAsJsonArray("addresses");
        for (JsonElement element : array) {
            String address = element.getAsJsonObject().get("address").getAsString();
            list.set(array.asList().indexOf(element), new KristAccount(address));
        }
        return list;
    }

    public static List<Transaction> getRecentTransactions(String address, int limit, int offset, boolean excludeMined) {
        List<Transaction> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address + "/transactions?limit=" + limit +
                        "&offset=" + offset + "&excludeMined=" + excludeMined, "GET"));
        JsonArray array = response.getAsJsonArray("transactions");
        for (JsonElement element : array) {
            list.set(array.asList().indexOf(element), new Transaction(element.getAsJsonObject()));
        }
        return list;
    }

    public static List<Name> getAllNames(String address, int limit, int offset) {
        List<Name> list = new ArrayList<>(limit);
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequest(
                KristURLConstants.KRIST_ADDRESSES + "/" + address + "/names?limit=" + limit +
                        "&offset=" + offset, "GET"));
        JsonArray array = response.getAsJsonArray("names");
        for (JsonElement element : array) {
            list.set(array.asList().indexOf(element), new Name(element.getAsJsonObject()));
        }
        return list;
    }

    public static KristAccount getAccountFromKey(String privateKey) {
        JsonObject response = KristUtil.validateResponse(Main.sendHTTPRequestWithContent(
                KristURLConstants.KRIST_ADDRESS_FROM_KEY, "POST", Map.of("privatekey", privateKey)));
        return new KristAccount(response.get("address").getAsString());
    }

    public static Pair<String, KristAccount> createNewAccount(String password) {
        String privatekey = KristUtil.getPrivateKeyFromPassword(password);
        JsonObject response = KristUtil.validateResponse(Main.manager.login(privatekey));
        Main.manager.logout();
        return new Pair<>(privatekey, new KristAccount(response.get("address").getAsJsonObject()));
    }

    // Methods for Updating All Accounts List

    private static List<KristAccount> getAllAccountsLoop(int totalAccounts) {
        List<KristAccount> list = new ArrayList<>(totalAccounts);
        for (int offset = 0; offset < totalAccounts; offset++) {
            JsonObject query = KristUtil.validateResponse(Main.sendHTTPRequest(
                    KristURLConstants.KRIST_ADDRESSES + "?limit=1&offset=" + offset, "GET"));
            JsonObject address = query.getAsJsonArray("addresses").get(0).getAsJsonObject();
            list.add(offset, new KristAccount(address));
        }
        return list;
    }

    private static void updateAllAccounts() {
        allAccounts.forEach(account -> {
            if (account.isOutdated())
                allAccounts.set(allAccounts.indexOf(account), new KristAccount(account.address()));
        });
    }
}
