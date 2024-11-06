package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final String regex;

    private RandomTrainerEntityFactory(String regex) {
        this.regex = regex;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, createRandomTrainer(), createRandomTexture());
    }

    private Identifier createRandomTrainer() {
        try {
            return new RandomTrainerFactory(this::isSatisfyAll).create();
        } catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Identifier createRandomTexture() {
        return new RandomTrainerTextureFactory().create();
    }

    private boolean isSatisfyAll(Identifier trainer) {
        return isMatchRegex(trainer) && isSpawningAllowed(trainer);
    }

    private boolean isMatchRegex(Identifier trainer) {
        return trainer.getPath().matches(regex);
    }

    private boolean isSpawningAllowed(Identifier trainer) {
        return TrainerProfileStorage.get(trainer).isSpawningAllowed();
    }

    public static class Builder {
        final String WILDCARD = ".*";

        private List<String> patterns;

        public Builder() {
            this.patterns = new ArrayList<>();
        }

        public Builder addWildcard() {
            patterns.add(WILDCARD);
            return this;
        }

        public Builder addRadicalRed() {
            patterns.add("radicalred");
            return this;
        }

        public Builder addInclementEmerald() {
            patterns.add("inclementemerald");
            return this;
        }

        public Builder addSmogon() {
            patterns.add("smogon");
            return this;
        }

        public RandomTrainerEntityFactory build() {
            if (patterns.isEmpty()) {
                throw new IllegalStateException();
            }

            if (patterns.contains(WILDCARD)) {
                 return new RandomTrainerEntityFactory(WILDCARD);
            } else {
                return new RandomTrainerEntityFactory(toRegex(patterns));
            }
        }

        private static String toRegex(List<String> patterns) {
            return String.format("^(%s)/.+", String.join("|", patterns));
        }
    }
}
