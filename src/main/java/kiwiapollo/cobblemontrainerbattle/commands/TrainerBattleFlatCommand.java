package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.InclementEmeraldTrainerFileScanner;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RadicalRedTrainerFileScanner;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFileScanner;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class TrainerBattleFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleFlatCommand() {
        super("trainerbattleflat");

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                .then(getRadicalRedTrainerBattleFlatCommand())
                .then(getInclementEmeraldTrainerBattleFlatCommand())
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(context -> {
                            TrainerBattle.battleWithFlatLevelAndFullHealth(context, new RandomTrainerFactory().create(context.getSource().getPlayer()));
                            return Command.SINGLE_SUCCESS;
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRadicalRedTrainerBattleFlatCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("radicalred")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            new RadicalRedTrainerFileScanner().getTrainerFiles().stream()
                                    .map(TrainerFileScanner::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String trainer = StringArgumentType.getString(context, "trainer");
                                TrainerBattle.battleWithFlatLevelAndFullHealth(context, new RadicalRedNameTrainerFactory().create(context.getSource().getPlayer(), trainer));
                                return Command.SINGLE_SUCCESS;

                            } catch (TrainerNameNotExistException e) {
                                String trainer = StringArgumentType.getString(context, "trainer");

                                context.getSource().getPlayer().sendMessage(
                                        Text.literal(String.format("Unknown trainer %s", trainer)));
                                CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                                CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getInclementEmeraldTrainerBattleFlatCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("inclementemerald")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            new InclementEmeraldTrainerFileScanner().getTrainerFiles().stream()
                                    .map(TrainerFileScanner::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String trainer = StringArgumentType.getString(context, "trainer");
                                TrainerBattle.battleWithFlatLevelAndFullHealth(context,
                                        new InclementEmeraldNameTrainerFactory().create(context.getSource().getPlayer(), trainer));
                                return Command.SINGLE_SUCCESS;

                            } catch (TrainerNameNotExistException e) {
                                String trainer = StringArgumentType.getString(context, "trainer");

                                context.getSource().getPlayer().sendMessage(
                                        Text.literal(String.format("Unknown trainer %s", trainer)));
                                CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                                CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                                return -1;
                            }
                        }));
    }
}
