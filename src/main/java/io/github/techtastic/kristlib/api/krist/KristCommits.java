package io.github.techtastic.kristlib.api.krist;

import com.google.gson.JsonObject;

/**
 * This is an object just to re-organize the commits gotten by JSON objects
 *
 * @param type the type of commit as a String
 * @param subject the subject of the commit as a String
 * @param body the description of the commit as a String
 * @param hash the commit's hash as a String
 * @param authorName the author of the commit
 * @param authorEmail the author's email
 * @param authorDate the date commited
 * @param authorDateRel
 * @param avatar the avatar of the author, if one is used
 */
public record KristCommits(
        String type,
        String subject,
        String body,
        String hash,
        String authorName,
        String authorEmail,
        String authorDate,
        String authorDateRel,
        String avatar
) {
    KristCommits(JsonObject commit) {
        this(
                commit.get("type") == null ? "none" : commit.get("type").getAsString(),
                commit.get("subject").getAsString(),
                commit.get("body").getAsString(),
                commit.get("hash").getAsString(),
                commit.get("authorName").getAsString(),
                commit.get("authorEmail").getAsString(),
                commit.get("authorDate").getAsString(),
                commit.get("authorDateRel").getAsString(),
                commit.get("avatar") == null ? "none" : commit.get("avatar").getAsString()
        );
    }
}
