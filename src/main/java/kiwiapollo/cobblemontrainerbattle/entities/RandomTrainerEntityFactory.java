package kiwiapollo.cobblemontrainerbattle.entities;

import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private static final Identifier FALLBACK_TRAINER = Identifier.of("trainer", "radicalred/youngster_joey");
    private final String regex;

    public RandomTrainerEntityFactory() {
        this(".*");
    }

    public RandomTrainerEntityFactory(String regex) {
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
            return FALLBACK_TRAINER;
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
        return ProfileRegistries.trainer.get(trainer).isSpawningAllowed();
    }
}
