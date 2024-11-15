package kiwiapollo.cobblemontrainerbattle.parser.profile;

import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;

public class TrainerProfileStorage {
    private static final ProfileRegistry<TrainerProfile> profiles = new ProfileRegistry<>();

    public static ProfileRegistry<TrainerProfile> getProfileRegistry() {
        return profiles;
    }
}
