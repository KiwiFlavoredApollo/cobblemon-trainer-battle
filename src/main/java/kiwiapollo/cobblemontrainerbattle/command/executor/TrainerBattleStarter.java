package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class TrainerBattleStarter implements Command<ServerCommandSource> {
    public int run(ServerPlayerEntity player, TrainerTemplate trainer) {
        try {
            new TrainerBattle(player, trainer).start();

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
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
        List<Identifier> random = new ArrayList<>(TrainerTemplateStorage.getInstance().keySet());
        Collections.shuffle(random);
        Identifier trainer = random.get(0);
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
