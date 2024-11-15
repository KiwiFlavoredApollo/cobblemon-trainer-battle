package kiwiapollo.cobblemontrainerbattle.parser.profile;

import kiwiapollo.cobblemontrainerbattle.groupbattle.TrainerGroupProfile;

public class TrainerGroupProfileStorage {
    private static final ProfileRegistry<TrainerGroupProfile> profiles = new ProfileRegistry<>();

    public static ProfileRegistry<TrainerGroupProfile> getProfileRegistry() {
        return profiles;
    }
}
