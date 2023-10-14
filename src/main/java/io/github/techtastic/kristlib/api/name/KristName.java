package io.github.techtastic.kristlib.api.name;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is for handling Krist names and their related information
 *
 * @author TechTastic
 */
public class KristName {
    private String name;
    private String owner;
    private String originalOwner;
    private String registration;
    private String lastUpdated;
    private String lastTransfer;
    private String data;
    private int unpaid;

    /**
     * Creates name from JsonObject
     *
     * @param name A JsonObject, usually received from responses from the Krist node
     */
    KristName(@NotNull JsonObject name) {
        this.name = name.get("name").getAsString();
        this.owner = name.get("owner").getAsString();
        this.originalOwner = name.get("original_owner").getAsString();
        this.registration = name.get("registered").getAsString();
        this.lastUpdated = name.get("updated").getAsString();
        this.lastTransfer = KristUtil.handleJsonNull(name.get("transferred"));
        this.data = KristUtil.handleJsonNull(name.get("a"));
        this.unpaid = name.get("unpaid").getAsInt();
    }

    /**
     * Creates name from the String version
     *
     * @param name A String of the name, usually received from responses from the Krist node
     */
    KristName(@NotNull String name) {
        this(KristHTTPHandler.getInfoFromHTTP(KristURLConstants.KRIST_NAMES_URL.getUrl() + "/" + name, HTTPRequestType.GET).getAsJsonObject("name"));
    }

    /**
     * This method is for getting the string version of the name
     *
     * @return the name as a String
     */
    @NotNull
    public String getName() {
        this.update();
        return this.name;
    }

    /**
     * This method is for getting the current owner's address
     *
     * @return the address of the current owner
     */
    @NotNull
    public String getCurrentOwner() {
        this.update();
        return this.owner;
    }

    /**
     * This method is for getting the original owner/purchaser's address, if there is one
     *
     * @return the address of the original owner or null
     */
    @Nullable
    public String getOriginalOwner() {
        this.update();
        return this.originalOwner;
    }

    /**
     * This method is for getting the date the name was purchased/registered
     *
     * @return the purchase/registration date of the name
     */
    @NotNull
    public String getRegistrationDate() {
        this.update();
        return this.registration;
    }

    /**
     * This method is for getting the date the name was last updated, whether by registration or transfer of ownership
     *
     * @return the last update date of the name, by registration or trade
     */
    @NotNull
    public String getLastUpdatedDate() {
        this.update();
        return this.lastUpdated;
    }

    /**
     * This method is for getting the date the name was last transfered, if at all
     *
     * @return the last transfer date of the name or null
     */
    @Nullable
    public String getLastTransferredDate() {
        this.update();
        return this.lastTransfer;
    }

    /**
     * This method is for getting any extra data stored in the name, if at all
     *
     * @return a String of extra data stored in the name or null
     */
    @Nullable
    public String getExtraData() {
        this.update();
        return this.data;
    }

    /**
     * This method returns the ynclaimed mining bonus all names have, starting at 500 upon registration
     *
     * @deprecated Mining is no longer available so this field is unused for the time being
     * @return the unpaid mining bonuses of the name, 0 - 500
     */
    @NotNull
    public Integer getUnpaidMiningBonuses() {
        return this.unpaid;
    }

    /**
     * This method is for updating the information of the name
     */
    public void update() {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(KristURLConstants.KRIST_NAMES_URL.getUrl() + "/" + name, HTTPRequestType.GET).getAsJsonObject("name");
        this.name = response.get("name").getAsString();
        this.owner = response.get("owner").getAsString();
        this.originalOwner = response.get("original_owner").getAsString();
        this.registration = response.get("registered").getAsString();
        this.lastUpdated = response.get("updated").getAsString();
        this.lastTransfer = KristUtil.handleJsonNull(response.get("transferred"));
        this.data = KristUtil.handleJsonNull(response.get("a"));
        this.unpaid = response.get("unpaid").getAsInt();
    }

    /**
     * This method is for getting the name as a JsonObject for use elsewhere
     *
     * @return the name as a JsonObject
     */
    @NotNull
    public JsonObject getAsJson() {
        this.update();

        JsonObject result = new JsonObject();
        result.addProperty("name", this.name);
        result.addProperty("owner", this.owner);
        result.addProperty("original_owner", this.originalOwner);
        result.addProperty("registered", this.registration);
        result.addProperty("updated", this.lastUpdated);
        result.addProperty("transferred", this.lastTransfer);
        result.addProperty("a", this.data);
        result.addProperty("unpaid", this.unpaid);

        return result;
    }

    @Override
    public String toString() {
        return this.getAsJson().toString();
    }
}
