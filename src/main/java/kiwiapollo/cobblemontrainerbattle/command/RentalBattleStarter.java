package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.RentalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.RentalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class RentalBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, String trainer) {
        try {
            TrainerBattle trainerBattle = new PlayerBackedTrainerBattle(
                    new RentalBattlePlayer(player),
                    new RentalBattleTrainer(trainer)
            );
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayer();
            String trainer = StringArgumentType.getString(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayer();
            String trainer = new RandomTrainerFactory().create();

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String trainer = StringArgumentType.getString(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            String trainer = new RandomTrainerFactory().create();

            return super.run(player, trainer);
        }
    }
}
