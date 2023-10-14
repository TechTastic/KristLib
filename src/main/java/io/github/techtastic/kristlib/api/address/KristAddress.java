package io.github.techtastic.kristlib.api.address;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import org.jetbrains.annotations.NotNull;

/**
* This class is for handling Krist addresses and their related information
*
* @author TechTastic
*/
public class KristAddress {
    private String address;
    private int balance;
    private int totalIncome;
    private int totalSpent;
    private String firstSeen;

    /**
     * Creates address from JsonObject
     *
     * @param address A JsonObject of the address, usually from responses from the Krist node
     */
    KristAddress(@NotNull JsonObject address) {
        this.address = address.get("address").getAsString();
        this.balance = address.get("balance") == null ? 0: address.get("balance").getAsInt();
        this.totalIncome = address.get("totalin") == null ? 0 : address.get("totalin").getAsInt();
        this.totalSpent = address.get("totalout") == null ? 0 : address.get("totalout").getAsInt();
        this.firstSeen = address.get("firstseen") == null ? "never" : address.get("firstseen").getAsString();
    }

    /**
     * Creates address from the String version
     *
     * @param address A String of the address, usually from responses from the Krist node
     */
    KristAddress(@NotNull String address) {
        this(KristHTTPHandler.getInfoFromHTTP(KristURLConstants.KRIST_ADDRESSES.getUrl()
                + "/" + address, HTTPRequestType.GET).getAsJsonObject("address"));
    }

    /**
     * This method is for getting the address as a string for use in other methods
     *
     * @return The address as a String
     */
    @NotNull
    public String getAddress() {
        this.update();
        return this.address;
    }

    /**
     * This method is for getting the balance of the address
     *
     * @return The balance of the address
     */
    @NotNull
    public Integer getBalance() {
        this.update();
        return this.balance;
    }

    /**
     * This method is for getting the total Krist received by this address
     *
     * @return The total income of the address
     */
    @NotNull
    public Integer getTotalIncome() {
        this.update();
        return this.totalIncome;
    }

    /**
     * This method is for getting the total Krist sent by this address
     *
     * @return The balance of the address
     */
    @NotNull
    public Integer getTotalSpent() {
        this.update();
        return this.totalSpent;
    }

    /**
     * This method is for getting the date of when this address is first used
     *
     * @return The date the address was first seen
     */
    @NotNull
    public String getFirstSeen() {
        this.update();
        return this.firstSeen;
    }

    /**
     * This method is for updating the information of the address
     */
    public void update() {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(KristURLConstants.KRIST_ADDRESSES.getUrl()
                + "/" + this.address, HTTPRequestType.GET).getAsJsonObject("address");

        this.address = response.get("address").getAsString();
        this.balance = response.get("balance").getAsInt();
        this.totalIncome = response.get("totalin").getAsInt();
        this.totalSpent = response.get("totalout").getAsInt();
        this.firstSeen = response.get("firstseen").getAsString();
    }

    /**
     * This method is for getting the address as a JsonObject for use elsewhere
     *
     * @return The address as a JsonObject
     */
    @NotNull
    public JsonObject getAsJson() {
        this.update();

        JsonObject result = new JsonObject();
        result.addProperty("address", this.address);
        result.addProperty("balance", this.balance);
        result.addProperty("totalin", this.totalIncome);
        result.addProperty("totalout", this.totalSpent);
        result.addProperty("firstseen", this.firstSeen);

        return result;
    }

    @Override
    public String toString() {
        return this.getAsJson().toString();
    }
}
