package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.google.gson.JsonObject;

public class GroupFile {
    public final JsonObject configuration;

    public GroupFile(JsonObject configuration) {
        this.configuration = configuration;
    }
}
