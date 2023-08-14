package io.github.techtastic.kristlib.api.transaction;

import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.KristUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;

/**
 * This class is for handling Krist transaction and their related information
 *
 * @author TechTastic
 */
public class KristTransaction {
    private final int id;
    private final String sender;
    private final String receiver;
    private final int value;
    private final String date;
    private final String name;
    private final CommonMeta metadata;
    private final String sentMetaname;
    private final String sentName;
    private final TransactionType type;

    /**
     * Creates transaction from JsonObject
     *
     * @param transaction A JsonObject, usually received from responses from the Krist node
     */
    KristTransaction(@NotNull JsonObject transaction) {
        this.id = transaction.get("id").getAsInt();
        this.sender = transaction.get("from").getAsString();
        this.receiver = transaction.get("to").getAsString();
        this.value = transaction.get("value").getAsInt();
        this.date = transaction.get("time").getAsString();
        this.name = transaction.get("name").getAsString();
        this.metadata = transaction.get("metadata").getAsString() == null ? null : new CommonMeta(transaction.get("metadata").getAsString());
        this.sentMetaname = transaction.get("sent_metaname").getAsString();
        this.sentName = transaction.get("sent_name").getAsString();
        this.type = TransactionType.valueOf(transaction.get("type").getAsString());
    }

    /**
     * Creates name from transaction ID
     *
     * @param id A transaction ID as an int, usually received from responses from the Krist node
     */
    KristTransaction(int id) {
        this(KristUtil.sendAndValidateHTTPRequest(
                KristURLConstants.KRIST_TRANSACTIONS_URL +
                        "/" + id, "GET"));
    }

    /**
     * This method is for getting the transtaction ID
     *
     * @return the transaction ID as an Integer
     */
    @NotNull
    public Integer getId() {
        return this.id;
    }

    /**
     * This method is for getting the address of the sender
     *
     * @return the sender's address as a String
     */
    @NotNull
    public String getSender() {
        return this.sender;
    }

    /**
     * This method is for getting the address of the receiver
     *
     * @return the receiver's address as a String
     */
    @NotNull
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * This method is for getting the amount of Krist exchanged
     *
     * @return the amount of Krist exchanged as an Integer
     */
    @NotNull
    public Integer getValue() {
        return this.value;
    }

    /**
     * This method is for getting the time of the transaction
     *
     * @return the time of the transaction as a Date
     */
    @NotNull
    public Date getDate() {
        return Date.valueOf(this.date);
    }

    /**
     * This method is for getting the recipient's name, if used
     *
     * @return the recipient's name as a String or null
     */
    @Nullable
    public String getName() {
        return this.name;
    }

    /**
     * This method is for getting the metadata added to the transaction, if any
     *
     * @return the metadata in CommonMeta format or null
     */
    @Nullable
    public CommonMeta getMetadata() {
        return this.metadata;
    }

    /**
     * This method is for getting the sender's metaname, if used
     *
     * @return the sender's metaname as a String or null
     */
    @Nullable
    public String getSentMetaname() {
        return this.sentMetaname;
    }

    /**
     * This method is for getting the sender's name, if used
     *
     * @return the sender's name as a String or null
     */
    @Nullable
    public String getSentName() {
        return this.sentName;
    }

    /**
     * This method is for getting the transaction type
     *
     * @return the transaction type as a TransactionType enum
     */
    @NotNull
    public TransactionType getType() {
        return this.type;
    }

    /**
     * This method is for getting the transaction as a JsonObject for use elsewhere
     *
     * @return the transaction as a JsonObject
     */
    @NotNull
    public JsonObject getAsJson() {
        JsonObject result = new JsonObject();

        result.addProperty("id", this.id);
        result.addProperty("from", this.sender);
        result.addProperty("to", this.receiver);
        result.addProperty("value", this.value);
        result.addProperty("time", this.date);
        result.addProperty("name", this.name);
        result.addProperty("metadata", this.metadata == null ? null :
                this.metadata.getRawMeta());
        result.addProperty("sent_metaname", this.sentMetaname);
        result.addProperty("sent_name", this.sentName);
        result.addProperty("type", this.type.toString());

        return result;
    }
}
