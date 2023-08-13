package io.github.techtastic.kristlib.api.transaction;

import org.jetbrains.annotations.Nullable;

/**
 * This class is for giving transaction metadata in CommonMeta format rather than a vague String
 *
 * @author TechTastic
 */
public class CommonMeta {
    private String meta;
    private String recipient;
    private String returnTo;
    private Boolean donate;
    private String username;
    private String message;
    private String error;
    private String epoch;

    /**
     * Creates CommonMeta from String
     *
     * @param meta the raw metadata as a String
     */
    CommonMeta(String meta) {
        this.meta = meta;
        if (meta != null) {
            this.recipient = parseRecipient(meta);
            this.returnTo = parseField(meta, "return");
            this.donate = parseBoolean(meta, "donate");
            this.username = parseField(meta, "username");
            this.message = parseField(meta, "message");
            this.error = parseField(meta, "error");
            this.epoch = parseField(meta, "epoch");
        }
    }

    /**
     * This method is for parsing the recipient out of the raw metadata, if there is one
     *
     * @param meta the raw metadata as a String
     * @return the recipient's name, metaname, or null
     */
    private String parseRecipient(String meta) {
        int kst = meta.indexOf(".kst");
        if (kst == -1)
            return null;
        return meta.substring(0, kst);
    }

    /**
     * This method is for parsing specific fields out of the raw metadata, if there are any fields
     *
     * @param meta the raw metadata as a String
     * @param field the name of the field to be parsed
     * @return the value of the field or null
     */
    private String parseField(String meta, String field) {
        int index = meta.indexOf(field + "=");
        if (index == -1)
            return null;
        int start = index + field.length();
        int end = meta.indexOf(";", index + 1);
        return end == -1 ? meta.substring(start) :
                meta.substring(start, end);
    }

    /**
     * This method is for parsing specific boolean fields out of the raw metadata, if there are any booleans
     *
     * @param meta the raw metadata as a String
     * @param field the name of the boolean field to be parsed
     * @return the value of the field or false if null
     */
    private boolean parseBoolean(String meta, String field) {
        String value = parseField(meta, field);
        if (value == null)
            return false;
        return Boolean.parseBoolean(value);
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
     * This method is for getting the recipient's name or metaname, if specified
     *
     * @return the recipient's name as a String, metaname as a String, or null
     */
    @Nullable
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * This method is for getting the return address or name, if specified
     *
     * @return the return address as a String, name as a String, or null
     */
    @Nullable
    public String getReturnAddressOrName() {
        return this.returnTo;
    }

    /**
     * This method is for signifiying if the transaction was a donation, even if left unspecificed
     *
     * @return true if it was a donation and false is it wasnt or wasnt speficied
     */
    @Nullable
    public Boolean isDonation() {
        return this.donate;
    }

    /**
     * This method is for getting the username of the sender, if specified
     *
     * @return the sender's username as a String or null
     */
    @Nullable
    public String getUsername() {
        return this.username;
    }

    /**
     * This method is for getting the embedded message, if specified
     *
     * @return the message as a String or null
     */
    @Nullable
    public String getMessage() {
        return this.message;
    }

    /**
     * This method is for getting the error, if specified
     *
     * @return the error as a String or null
     */
    @Nullable
    public String getError() {
        return this.error;
    }

    /**
     * Thisa method is for getting the Unix epoch time, if specified
     *
     * @return the epoch time as a String or null
     */
    @Nullable
    public String getEpochTime() {
        return this.epoch;
    }
}