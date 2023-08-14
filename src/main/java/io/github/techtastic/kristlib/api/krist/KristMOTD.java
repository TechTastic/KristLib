package io.github.techtastic.kristlib.api.krist;

import com.google.gson.JsonObject;

import java.sql.Date;

/**
 * This class is for the storage and reference of the MOTD and general Krist server info
 *
 * @param motd the Message of the Day as a String
 * @param set the date the MOTD was set in an older format as a String
 * @param motdSet the date the MOTD was set as a Date
 * @param serverTime the current date according to the Krist network as a Date
 * @param publicUrl the public URL of the Krist network as a String
 * @param publicWSUrl the public WebSocket gateway address as a String
 * @param debugMode whether the Krist network is in debug mode as a boolean
 * @param kristPackage information about the current Krist source code as a KristPackage
 * @param constants the constants of the Krist network as a KristConstants
 * @param currency information about the currency of the Krist network as a KristCurrency
 * @param notice the copyright information regarding Krist and its source code
 */
public record KristMOTD(
        String motd,
        String set,
        Date motdSet,
        Date serverTime,
        String publicUrl,
        String publicWSUrl,
        boolean debugMode,
        KristPackage kristPackage,
        KristConstants constants,
        KristCurrency currency,
        String notice
) {
    /**\
     * Creats a KristMOTD from a JsonObject, obtained from the Krist network
     *
     * @param motd the MOTD as a JsonObject
     */
    KristMOTD(JsonObject motd) {
        this(
                motd.get("motd").getAsString(),
                motd.get("set").getAsString(),
                Date.valueOf(motd.get("motd_set").getAsString()),
                Date.valueOf(motd.get("server_time").getAsString()),
                motd.get("public_url").getAsString(),
                motd.get("public_ws_url").getAsString(),
                motd.get("debug_mode").getAsBoolean(),
                new KristPackage(motd.get("package").getAsJsonObject()),
                new KristConstants(motd.get("constants").getAsJsonObject()),
                new KristCurrency(motd.get("currency").getAsJsonObject()),
                motd.get("notice").getAsString()
        );
    }

    public record KristPackage(
            String name,
            String version,
            String author,
            String license,
            String repository
    ) {
        KristPackage(JsonObject pack) {
            this(
                    pack.get("name").getAsString(),
                    pack.get("version").getAsString(),
                    pack.get("author").getAsString(),
                    pack.get("license").getAsString(),
                    pack.get("repository").getAsString()
            );
        }
    }

    public record KristConstants(
            String walletVersion,
            int nameCost
    ) {
        KristConstants(JsonObject constants) {
            this(
                    constants.get("wallet_version").getAsString(),
                    constants.get("name_cost").getAsInt()
            );
        }
    }

    public record KristCurrency(
            String addressPrefix,
            String nameSuffix,
            String currencyName,
            String currencySymbol
    ) {
        KristCurrency(JsonObject currency) {
            this(
                    currency.get("address_prefix").getAsString(),
                    currency.get("name_suffix").getAsString(),
                    currency.get("currency_name").getAsString(),
                    currency.get("currency_symbol").getAsString()
            );
        }
    }
}
