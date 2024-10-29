package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.FlatBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.FlatBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.common.ResourceValidator;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.postbattle.BatchedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.RecordedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleRegistry;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

public class TrainerBattleFlatOtherCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleFlatOtherCommand() {
        super("trainerbattleflatother");

        this.requires(new PlayerCommandPredicate(
                        String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
                ))
                .then(getSpecificTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSpecificTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                .requires(new PlayerCommandPredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            ProfileRegistries.trainer.keySet().stream()
                                    .map(Identifier::toString)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(this::makePlayerStartBattleWithSelectedTrainer));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                .requires(new PlayerCommandPredicate(permission))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(permission))
                        .executes(this::makePlayerStartBattleWithRandomTrainer));
    }

    private int makePlayerStartBattleWithSelectedTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            Identifier trainer = new Identifier(StringArgumentType.getString(context, "trainer"));

            return startBattleWithTrainer(player, trainer);

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;
        }
    }

    private int makePlayerStartBattleWithRandomTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            Identifier trainer = new RandomTrainerFactory().create();

            return startBattleWithTrainer(player, trainer);

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;
        }
    }

    private int startBattleWithTrainer(ServerPlayerEntity player, Identifier trainer) {
        try {
            ResourceValidator.assertTrainerExist(trainer);

            PlayerBattleParticipant playerBattleParticipant = new FlatBattlePlayer(player, StandardTrainerBattle.FLAT_LEVEL);
            TrainerBattleParticipant trainerBattleParticipant = new FlatBattleTrainer(trainer, player, StandardTrainerBattle.FLAT_LEVEL);

            TrainerProfile profile = ProfileRegistries.trainer.get(trainer);
            BattleResultHandler battleResultHandler = new BatchedBattleResultHandler(
                    new RecordedBattleResultHandler(player, trainer),
                    new PostBattleActionSetHandler(player, profile.onVictory(), profile.onDefeat())
            );

            TrainerBattle trainerBattle = new StandardTrainerBattle(playerBattleParticipant, trainerBattleParticipant, battleResultHandler);
            trainerBattle.start();

            TrainerBattleRegistry.put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (FileNotFoundException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.common.resource.trainer_not_found"));
            return 0;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
