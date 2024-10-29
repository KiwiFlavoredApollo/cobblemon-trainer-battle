package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.NormalTrainerBattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class TrainerBattleOtherCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleOtherCommand() {
        super("trainerbattleother");

        List<String> permissions = List.of(
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
        );

        this.requires(new PlayerCommandPredicate(permissions.toArray(String[]::new)))
                .then(getSelectedTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSelectedTrainerBattleCommand() {
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

            return CommandTrainerBattleStarter.startBattle(player, trainer, new NormalTrainerBattleParticipantFactory());

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;
        }
    }

    private int makePlayerStartBattleWithRandomTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            Identifier trainer = new RandomTrainerFactory().create();

            return CommandTrainerBattleStarter.startBattle(player, trainer, new NormalTrainerBattleParticipantFactory());

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;
        }
    }
}
