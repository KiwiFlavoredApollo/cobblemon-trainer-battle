package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.EntityBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.EntityBackedTrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.random.RandomTrainerBattleTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class EntityBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, String trainer, LivingEntity entity) {
        try {
            TrainerBattle trainerBattle = new PlayerBackedTrainerBattle(
                    new PlayerBattleParticipantFactory(player, getLevelmode(trainer)).create(),
                    new EntityBackedTrainerBattleParticipantFactory(trainer, entity).create()
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private LevelMode getLevelmode(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getLevelMode();
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends EntityBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String uuid = StringArgumentType.getString(context, "uuid");
            String trainer = StringArgumentType.getString(context, "trainer");
            LivingEntity entity = (LivingEntity) context.getSource().getServer().getWorld(World.OVERWORLD).getEntity(UUID.fromString(uuid));

            return super.run(player, trainer, entity);
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends EntityBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String uuid = StringArgumentType.getString(context, "uuid");
            String trainer = new RandomTrainerBattleTrainerFactory().create();
            LivingEntity entity = (LivingEntity) context.getSource().getServer().getWorld(World.OVERWORLD).getEntity(UUID.fromString(uuid));

            return super.run(player, trainer, entity);
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends EntityBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String uuid = StringArgumentType.getString(context, "uuid");
            String trainer = StringArgumentType.getString(context, "trainer");
            LivingEntity entity = (LivingEntity) context.getSource().getServer().getWorld(World.OVERWORLD).getEntity(UUID.fromString(uuid));

            return super.run(player, trainer, entity);
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends EntityBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String uuid = StringArgumentType.getString(context, "uuid");
            String trainer = new RandomTrainerBattleTrainerFactory().create();
            LivingEntity entity = (LivingEntity) context.getSource().getServer().getWorld(World.OVERWORLD).getEntity(UUID.fromString(uuid));

            return super.run(player, trainer, entity);
        }
    }
}
