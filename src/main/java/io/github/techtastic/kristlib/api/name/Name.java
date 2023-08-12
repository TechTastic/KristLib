package io.github.techtastic.kristlib.api.name;

import com.google.gson.JsonObject;

public record Name(
        String name,
        String owner,
        String originalOwner,
        String registered,
        String updated,
        String transferred,
        String a,
        int unpaid
) {
    public Name(JsonObject name) {
        this(
                name.get("name").getAsString(),
                name.get("owner").getAsString(),
                name.get("oirinal_owner").getAsString(),
                name.get("registered").getAsString(),
                name.get("updated").getAsString(),
                name.get("transferred").getAsString(),
                name.get("a").getAsString(),
                name.get("unpaid").getAsInt()
        );
    }
}
