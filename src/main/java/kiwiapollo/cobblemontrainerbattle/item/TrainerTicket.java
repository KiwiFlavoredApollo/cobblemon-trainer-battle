package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class TrainerTicket extends Item {
    private final EntityType.EntityFactory<TrainerEntity> factory;

    public TrainerTicket(Settings settings, EntityType.EntityFactory<TrainerEntity> factory) {
        super(settings);
        this.factory = factory;
    }

    public TrainerEntity createTrainerEntity(EntityType<TrainerEntity> type, World world) {
        return factory.create(type, world);
    }
}
