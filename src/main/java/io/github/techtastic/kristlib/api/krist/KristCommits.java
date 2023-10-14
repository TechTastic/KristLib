package io.github.techtastic.kristlib.api.krist;

import com.google.gson.JsonObject;

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
