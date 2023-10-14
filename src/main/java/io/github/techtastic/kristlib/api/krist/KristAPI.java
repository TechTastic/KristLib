package io.github.techtastic.kristlib.api.krist;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.techtastic.kristlib.util.KristURLConstants;
import io.github.techtastic.kristlib.util.http.HTTPRequestType;
import io.github.techtastic.kristlib.util.http.KristHTTPHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for getting extra information from the Krist network
 *
 * @author TechTastic
 */
public class KristAPI {
    /**
     * This method is for getting the Message of the Day as well as general server information from the Krist network
     *
     * @return the server info and MOTD as a KristMOTD
     */
    @NotNull
    public static KristMOTD getMOTD() {
        return new KristMOTD(KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_SERVER_INFO, HTTPRequestType.GET));
    }

    /**
     * This method is for getting the last 10 commits/code changes for the Krist network
     *
     * @return the last 10 commits as a List of KristCommits
     */
    @NotNull
    public static List<KristCommits> getLatestChanges() {
        List<KristCommits> list = new ArrayList<>(10);
        JsonObject response = KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_CHANGELOG, HTTPRequestType.GET);

        JsonArray commits = response.getAsJsonArray("commits");
        for (JsonElement element : commits) {
            list.add(commits.asList().indexOf(element),
                    new KristCommits(element.getAsJsonObject()));
        }

        return list;
    }

    /**
     * This method is for getting the total amount of existing Krist across the entire network
     *
     * @return all existing Krist as an Integer
     */
    @NotNull
    public static Integer getTotalExistingKrist() {
        return KristHTTPHandler.getInfoFromHTTP(
                KristURLConstants.KRIST_MONEY_SUPPLY, HTTPRequestType.GET).get("money_supply").getAsInt();
    }
}
