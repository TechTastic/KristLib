package io.github.techtastic.kristlib.api.address;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import io.github.techtastic.kristlib.util.websocket.KristWSSHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used for working with the KristAddress class via multiple methods
 *
 * @author TechTastic
 */
public class AddressAPI {
    /**
     * This method is for creating a completely new Krist address via the Krist node
     *
     * @param privatekey the private key of the new address
     * @return the newly created address as a KristAddress
     */
    public static KristAddress createNewAddress(String privatekey, KristWSSHandler websocket) {
        // Get v2 address from Private Key
        KristAddress address = getAddressFromPrivateKey(privatekey);

        // Create address info by authentication
        websocket.login(privatekey);
        websocket.logout();

        address.update();

        // Return newly created KristAddress from the vs2 address
        return address;
    }

    /**
     * This method is for getting an address as a KristAddress from a String
     *
     * @param address the address as a String
     * @return the address as a KristAddress
     */
    public static KristAddress getAddress(String address) {
        return new KristAddress(address);
    }

    /**
     * This method is for getting an address as a KristAddress from a JsxonObject
     *
     * @param address the address as a JsonObject
     * @return the address as a KristAddress
     */
    public static KristAddress getAddress(JsonObject address) {
        return new KristAddress(address);
    }

    /**
     * This method is for getting an address as a KristAddress from a private key as a String
     *
     * @param privatekey the private key as a String
     * @return the address as a KristAddress belonging to the key
     */
    public static KristAddress getAddressFromPrivateKey(String privatekey) {
        return new KristAddress(KristHTTPHandler.getInfoFromHTTPWithContent(
                KristURLConstants.KRIST_ADDRESS_FROM_KEY, HTTPRequestType.POST, Map.of("privatekey", privatekey)));
    }

    /**
     * This method is for getting all addresses from the Krist node with the limit and offset query data
     *
     * @param limit the number of addresses to return, 1 - 1000
     * @param offset the number of addresses to skip over
     * @return the List of addresses as KristAddress
     */
    public static List<KristAddress> getAllAddresses(int limit, int offset) {
        List<KristAddress> list = new ArrayList<>(limit);
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_ADDRESSES.getUrl() + "?limit=" + limit +
                        "&offset=" + offset, HTTPRequestType.GET);

        JsonArray addresses = response.getAsJsonArray("addresses");
        for (JsonElement element : addresses) {
            list.add(addresses.asList().indexOf(element),
                    new KristAddress(element.getAsJsonObject()));
        }

        return list;
    }

    /**
     * This method is for getting the total number of address in use on the Krist network
     *
     * @return the total number of used addresses asa an Integer
     */
    public static Integer getTotalAddressCount() {
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_ADDRESSES.getUrl() + "?limit=" + 1, HTTPRequestType.GET);
        return response.get("total").getAsInt();
    }
}
