package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;

public class DeprecatedEntityMigrator implements ServerEntityEvents.Load {
    private static final Map<EntityType<? extends LivingEntity>, EntityType<? extends LivingEntity>> ENTITY = Map.ofEntries(
            Map.entry(CustomEntityType.STATIC_TRAINER, CustomEntityType.MANNEQUIN),
            Map.entry(CustomEntityType.NEUTRAL_TRAINER, CustomEntityType.TRAINER)
    );

    public static void initialize() {
        ServerEntityEvents.ENTITY_LOAD.register(new DeprecatedEntityMigrator());
    }

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        for (Map.Entry<EntityType<? extends LivingEntity>, EntityType<? extends LivingEntity>> entry : ENTITY.entrySet()) {
            EntityType<? extends LivingEntity> oldEntityType = entry.getKey();
            EntityType<? extends LivingEntity> newEntityType = entry.getValue();
            try {
                if (world.isClient()) {
                    continue;
                }

                if (entity.getType() != oldEntityType) {
                    continue;
                }

                LivingEntity newEntity = newEntityType.create(world);

                newEntity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());

                NbtCompound tag = new NbtCompound();
                entity.writeNbt(tag);
                tag.remove("UUID");
                tag.remove("UUIDMost");
                tag.remove("UUIDLeast");
                newEntity.readNbt(tag);

                world.spawnEntity(newEntity);

                entity.remove(Entity.RemovalReason.DISCARDED);

            } catch (Exception ignored) {

            }
        }
    }
}
