package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.RentalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.RentalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemonStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RentalBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, Identifier trainer) {
        try {
            if (!isKnownTrainer(trainer)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalbattle.unknown_trainer", trainer).formatted(Formatting.RED));
                return 0;
            }

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

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private boolean isKnownTrainer(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().keySet().contains(trainer);
    }

    private boolean hasRentalBattleMinimumPartySize(ServerPlayerEntity player) {
        return RentalPokemonStorage.getInstance().get(player).occupied() >= RentalBattlePreset.PARTY_SIZE;
    }

    private boolean hasRentalBattleMinimumPartySize(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().get(trainer).getTeam().size() >= RentalBattlePreset.PARTY_SIZE;
    }

    protected ServerPlayerEntity getThisPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return context.getSource().getPlayerOrThrow();
    }

    protected ServerPlayerEntity getOtherPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return EntityArgumentType.getPlayer(context, "player");
    }

    protected Identifier getSelectedTrainer(CommandContext<ServerCommandSource> context) {
        return IdentifierArgumentType.getIdentifier(context, "trainer");
    }

    protected Identifier getRandomTrainer(CommandContext<ServerCommandSource> context) {
        List<Identifier> trainers = TrainerTemplateStorage.getInstance().keySet().stream().filter(this::hasRentalBattleMinimumPartySize).toList();
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);
        return random.get(0);
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getThisPlayer(context), getSelectedTrainer(context));
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getThisPlayer(context), getRandomTrainer(context));
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getOtherPlayer(context), getSelectedTrainer(context));
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends RentalBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getOtherPlayer(context), getRandomTrainer(context));
        }
    }
}
