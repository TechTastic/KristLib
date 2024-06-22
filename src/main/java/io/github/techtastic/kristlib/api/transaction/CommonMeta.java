package io.github.techtastic.kristlib.api.transaction;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * This class is for giving transaction metadata in CommonMeta format rather than a vague String
 *
 * @author TechTastic
 */
public class CommonMeta {
    private final HashMap<String, Object> metadata = new HashMap<String,Object>();
    private final String meta;

    /**
     * Creates CommonMeta from String
     *
     * @param meta the raw metadata as a String
     */
    CommonMeta(String meta) {
        this.meta = meta;
        if (meta != null) {
            String sub = meta;
            while (!sub.isEmpty()) {
                int kst = sub.indexOf(".kst");
                if (kst != -1) {
                    this.metadata.put("recipient", sub.substring(0, kst));
                    sub = sub.replace((String) this.metadata.get("recipient"), "");
                }

                int eq = sub.indexOf("=");
                int sc = sub.indexOf(";");
                if (eq == -1 && sc == -1) break;
                String field = sub.substring(0, eq);
                String value = sub.substring(eq + 1, sc);
                this.metadata.put(field, value);
                sub = sub.replace(field + "=" + value + ";", "");
            }
        }
    }

    /**
     * This method is for getting the raw metadata as a String
     *
     * @return the raw metadata as a String
     */
    @Nullable
    public String getRawMeta() {
        return this.meta;
    }

    /**
     * This method is for attempting ot grab a specific field from the metadata, can be null
     *
     * @param field the requested field
     * @return the field's value or null
     */
    @Nullable
    public Object getFromField(String field) {
        return this.metadata.get(field);
    }

    /**
     * This method is for getting the recipient's name or metaname, if specified
     *
     * @return the recipient's name as a String, metaname as a String, or null
     */
    @Nullable
    public String getRecipient() {
        return (String) this.metadata.get("recipient");
    }

    /**
     * This method is for getting the return address or name, if specified
     *
     * @return the return address as a String, name as a String, or null
     */
    @Nullable
    public String getReturnAddressOrName() {
        return (String) this.metadata.get("return");
    }

    /**
     * This method is for signifiying if the transaction was a donation, even if left unspecificed
     *
     * @return true if it was a donation and false is it wasnt or wasnt speficied
     */
    @Nullable
    public Boolean isDonation() {
        return Boolean.parseBoolean((String) this.metadata.get("donate"));
    }

    /**
     * This method is for getting the username of the sender, if specified
     *
     * @return the sender's username as a String or null
     */
    @Nullable
    public String getUsername() {
        return (String) this.metadata.get("username");
    }

    /**
     * This method is for getting the embedded message, if specified
     *
     * @return the message as a String or null
     */
    @Nullable
    public String getMessage() {
        return (String) this.metadata.get("message");
    }

    /**
     * This method is for getting the error, if specified
     *
     * @return the error as a String or null
     */
    @Nullable
    public String getError() {
        return (String) this.metadata.get("error");
    }

    /**
     * Thisa method is for getting the Unix epoch time, if specified
     *
     * @return the epoch time as a String or null
     */
    @Nullable
    public String getEpochTime() {
        return (String) this.metadata.get("epoch");
    }
}