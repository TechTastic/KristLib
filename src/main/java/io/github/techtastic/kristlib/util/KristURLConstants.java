package io.github.techtastic.kristlib.util;

public class KristURLConstants {
    public static String KRIST_BASE_URL;
    public static String KRIST_WEBSOCKET_INITIALIZE;
    public static String KRIST_ADDRESSES;
    public static String KRIST_TRANSACTIONS_URL;
    public static String KRIST_NAMES_URL;
    public static String KRIST_NAME_COST;
    public static String KRIST_NEW_NAMES;
    public static String KRIST_MONEY_SUPPLY;
    public static String KRIST_CHANGELOG;
    public static String KRIST_KRISTWALLET_VERSION;
    public static String KRIST_SERVER_INFO;
    public static String KRIST_AUTHENTICATE_ACCESS;
    public static String KRIST_SEARCH_NETWORK;
    public static String KRIST_RICHEST;
    public static String KRIST_ADDRESS_FROM_KEY;

    static {
        KRIST_BASE_URL = "https://krist.dev";
        KRIST_WEBSOCKET_INITIALIZE = "https://krist.dev/ws/start";
        KRIST_ADDRESSES = "https://krist.dev/addresses";
        KRIST_TRANSACTIONS_URL = "https://krist.dev/transactions";
        KRIST_NAMES_URL = "https://krist.dev/names";
        KRIST_NAME_COST = "https://krist.dev/names/cost";
        KRIST_NEW_NAMES = "https://krist.dev/names/new";
        KRIST_MONEY_SUPPLY = "https://krist.dev/supply";
        KRIST_CHANGELOG = "https://krist.dev/whatsnew";
        KRIST_KRISTWALLET_VERSION = "https://krist.dev/walletversion";
        KRIST_SERVER_INFO = "https://krist.dev/motd";
        KRIST_AUTHENTICATE_ACCESS = "https://krist.dev/login";
        KRIST_SEARCH_NETWORK = "https://krist.dev/search";
        KRIST_RICHEST = "https://krist.dev/addresses/rich";
        KRIST_ADDRESS_FROM_KEY = "https://krist.dev/v2";
    }
}
