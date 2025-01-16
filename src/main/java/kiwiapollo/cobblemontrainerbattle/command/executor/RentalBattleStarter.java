package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.RentalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.RentalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.random.RandomRentalBattleTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class RentalBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, String trainer) {
        try {
            if (!hasRentalBattleMinimumPartySize(player)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalbattle.player_minimum_party_size", RentalBattlePreset.PARTY_SIZE));
                return 0;
            }

            if (!hasRentalBattleMinimumPartySize(trainer)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalbattle.trainer_minimum_party_size", RentalBattlePreset.PARTY_SIZE));
                return 0;
            }

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

    private boolean hasRentalBattleMinimumPartySize(ServerPlayerEntity player) {
        return BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getRentalPokemon().occupied() >= RentalBattlePreset.PARTY_SIZE;
    }

    private boolean hasRentalBattleMinimumPartySize(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getParty().occupied() >= RentalBattlePreset.PARTY_SIZE;
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String trainer = StringArgumentType.getString(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            String trainer = new RandomRentalBattleTrainerFactory().create();

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
            String trainer = new RandomRentalBattleTrainerFactory().create();

            return super.run(player, trainer);
        }
    }
}
