package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;

public class LegacyEntityMigrator implements ServerEntityEvents.Load {
    private static final Map<EntityType<? extends LivingEntity>, EntityType<? extends LivingEntity>> ENTITY = Map.ofEntries(
            Map.entry(CustomEntityType.STATIC_TRAINER, CustomEntityType.CAMPER),
            Map.entry(CustomEntityType.NEUTRAL_TRAINER, CustomEntityType.DRIFTER)
    );

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        ENTITY.forEach((oldEntityType, newEntityType) -> {
            try {
                if (world.isClient()) {
                    return;
                }

                if (entity.getType() != oldEntityType) {
                    return;
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
        });
    }
}
