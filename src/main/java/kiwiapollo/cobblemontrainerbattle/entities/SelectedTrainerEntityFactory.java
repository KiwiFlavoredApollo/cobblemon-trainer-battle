package kiwiapollo.cobblemontrainerbattle.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SelectedTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final Identifier trainer;
    private final Identifier texture;

    public SelectedTrainerEntityFactory(Identifier trainer, Identifier texture) {
        this.trainer = trainer;
        this.texture = texture;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, trainer, texture);
    }
}
