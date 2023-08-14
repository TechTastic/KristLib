package io.github.techtastic.kristlib.api.krist;

import com.google.gson.JsonObject;

import java.sql.Date;

public record KristCommits(
        CommitType type,
        String subject,
        String body,
        String hash,
        String authorName,
        String authorEmail,
        Date authorDate,
        String authorDateRel,
        String avatar
) {
    KristCommits(JsonObject commit) {
        this(
                commit.get("type").getAsString() == null ? CommitType.NONE :
                        CommitType.valueOf(commit.get("type").getAsString()),
                commit.get("subject").getAsString(),
                commit.get("body").getAsString(),
                commit.get("hash").getAsString(),
                commit.get("authorName").getAsString(),
                commit.get("authorEmail").getAsString(),
                Date.valueOf(commit.get("authorDate").getAsString()),
                commit.get("authorDateRel").getAsString(),
                commit.get("avatar").getAsString()
        );
    }

    public enum CommitType {
        BUILD,
        CHORE,
        CI,
        DOCS,
        FEAT,
        FIX,
        PERF,
        REFACTOR,
        REVERT,
        STYLE,
        TEST,
        NONE
    }
}
