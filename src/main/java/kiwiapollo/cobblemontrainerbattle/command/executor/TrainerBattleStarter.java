package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.CustomIdentifierArgumentType;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.battle.random.RandomTrainerBattleTrainerFactory;
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

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);

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

    public static class BetweenThisPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            Identifier trainer = CustomIdentifierArgumentType.getIdentifier(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenThisPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            Identifier trainer = new RandomTrainerBattleTrainerFactory().create();

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndSelectedTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            Identifier trainer = CustomIdentifierArgumentType.getIdentifier(context, "trainer");

            return super.run(player, trainer);
        }
    }

    public static class BetweenOtherPlayerAndRandomTrainer extends TrainerBattleStarter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            Identifier trainer = new RandomTrainerBattleTrainerFactory().create();

            return super.run(player, trainer);
        }
    }
}
