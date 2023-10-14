package io.github.techtastic.kristlib.util;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is a helpful constants class for interactions with HTTP and WSS requests to the Krist node
 *
 * @author TechTastic
 */
public enum KristURLConstants {
    KRIST_BASE_URL("https://krist.dev"),
    KRIST_WEBSOCKET_INITIALIZE("https://krist.dev/ws/start"),
    KRIST_ADDRESSES("https://krist.dev/addresses"),
    KRIST_TRANSACTIONS_URL("https://krist.dev/transactions"),
    KRIST_NAMES_URL("https://krist.dev/names"),
    KRIST_NAME_COST("https://krist.dev/names/cost"),
    KRIST_NEW_NAMES("https://krist.dev/names/new"),
    KRIST_MONEY_SUPPLY("https://krist.dev/supply"),
    KRIST_CHANGELOG("https://krist.dev/whatsnew"),
    KRIST_KRISTWALLET_VERSION("https://krist.dev/walletversion"),
    KRIST_SERVER_INFO("https://krist.dev/motd"),
    KRIST_AUTHENTICATE_ACCESS("https://krist.dev/login"),
    KRIST_SEARCH_NETWORK("https://krist.dev/search"),
    KRIST_RICHEST("https://krist.dev/addresses/rich"),
    KRIST_ADDRESS_FROM_KEY("https://krist.dev/v2");

    String url;

    KristURLConstants(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public KristURLConstants changeUrl(String newUrl) {
        this.url = newUrl;
        return this;
    }
}
