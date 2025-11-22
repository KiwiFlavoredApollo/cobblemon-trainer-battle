package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
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

public abstract class TrainerBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, Identifier trainer) {
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

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private boolean isKnownTrainer(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().keySet().contains(trainer);
    }

    private LevelMode getLevelmode(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().get(trainer).getLevelMode();
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
        List<Identifier> random = new ArrayList<>(TrainerTemplateStorage.getInstance().keySet());
        Collections.shuffle(random);
        return random.get(0);
    }

    public static class BetweenThisPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getThisPlayer(context), getSelectedTrainer(context));
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getThisPlayer(context), getRandomTrainer(context));
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getOtherPlayer(context), getSelectedTrainer(context));
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getOtherPlayer(context), getRandomTrainer(context));
        }
    }
}
