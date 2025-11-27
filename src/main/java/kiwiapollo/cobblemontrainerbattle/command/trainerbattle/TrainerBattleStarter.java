package kiwiapollo.cobblemontrainerbattle.command.trainerbattle;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.template.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public abstract class TrainerBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, TrainerTemplate trainer) {
        try {
            if (isTrainerExist(trainer)) {
                player.sendMessage(getNoTrainerErrorMessage());
                return 0;
            }

            new TrainerBattle(player, trainer).start();

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private boolean isTrainerExist(TrainerTemplate trainer) {
        return trainer == null;
    }

    private Text getNoTrainerErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.trainerbattle.failed.no_trainer").formatted(Formatting.RED);
    }

    protected ServerPlayerEntity getThisPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return context.getSource().getPlayerOrThrow();
    }

    protected ServerPlayerEntity getOtherPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return EntityArgumentType.getPlayer(context, "player");
    }

    protected TrainerTemplate getSelectedTrainer(CommandContext<ServerCommandSource> context) {
        Identifier trainer = IdentifierArgumentType.getIdentifier(context, "trainer");
        return TrainerTemplateStorage.getInstance().get(trainer);
    }

    protected TrainerTemplate getRandomTrainer(CommandContext<ServerCommandSource> context) {
        Identifier trainer = new RandomTrainerFactory().create();
        return TrainerTemplateStorage.getInstance().get(trainer);
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
