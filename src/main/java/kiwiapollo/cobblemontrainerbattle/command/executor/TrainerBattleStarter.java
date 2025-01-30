package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.battle.random.RandomTrainerBattleTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class TrainerBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, String trainer) {
        try {
            if (!isKnownTrainer(trainer)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.trainerbattle.unknown_trainer", trainer).formatted(Formatting.RED));
                return 0;
            }

            TrainerBattle trainerBattle = new PlayerBackedTrainerBattle(
                    new PlayerBattleParticipantFactory(player, getLevelmode(trainer)).create(),
                    new TrainerBattleParticipantFactory(trainer).create()
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private boolean isKnownTrainer(String trainer) {
        return TrainerStorage.getInstance().keySet().contains(trainer);
    }

    private LevelMode getLevelmode(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getLevelMode();
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String trainer = StringArgumentType.getString(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String trainer = new RandomTrainerBattleTrainerFactory().create();

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String trainer = StringArgumentType.getString(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String trainer = new RandomTrainerBattleTrainerFactory().create();

            return super.run(player, trainer);
        }
    }
}
