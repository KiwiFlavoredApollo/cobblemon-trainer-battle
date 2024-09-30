package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleCommand() {
        super("trainerbattle");

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                .then(getRadicalRedTrainerBattleCommand())
                .then(getInclementEmeraldTrainerBattleCommand())
                .then(getCustomTrainerBattleCommand())
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(context -> {
                            TrainerBattle.battleWithStatusQuo(context, new TotalRandomTrainerFactory().create(context.getSource().getPlayer()));
                            return Command.SINGLE_SUCCESS;
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRadicalRedTrainerBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("radicalred")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.RADICAL_RED_TRAINERS.stream()
                                    .map(TrainerFileHelper::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String trainer = StringArgumentType.getString(context, "trainer");
                                TrainerBattle.battleWithStatusQuo(context, new RadicalRedNameTrainerFactory().create(context.getSource().getPlayer(), trainer));
                                return Command.SINGLE_SUCCESS;

                            } catch (TrainerNameNotExistException e) {
                                String trainer = StringArgumentType.getString(context, "trainer");

                                context.getSource().getPlayer().sendMessage(
                                        Text.literal(String.format("Unknown trainer %s", trainer)).formatted(Formatting.RED));
                                CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                                CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getInclementEmeraldTrainerBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("inclementemerald")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.INCLEMENT_EMERALD_TRAINERS.stream()
                                    .map(TrainerFileHelper::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String trainer = StringArgumentType.getString(context, "trainer");
                                TrainerBattle.battleWithStatusQuo(context,
                                        new InclementEmeraldNameTrainerFactory().create(context.getSource().getPlayer(), trainer));
                                return Command.SINGLE_SUCCESS;

                            } catch (TrainerNameNotExistException e) {
                                String trainer = StringArgumentType.getString(context, "trainer");

                                context.getSource().getPlayer().sendMessage(
                                        Text.literal(String.format("Unknown trainer %s", trainer)).formatted(Formatting.RED));
                                CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                                CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getCustomTrainerBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("custom")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.CUSTOM_TRAINERS.stream()
                                    .map(TrainerFileHelper::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String trainer = StringArgumentType.getString(context, "trainer");
                                TrainerBattle.battleWithStatusQuo(context, new CustomNameTrainerFactory().create(context.getSource().getPlayer(), trainer));
                                return Command.SINGLE_SUCCESS;

                            } catch (TrainerNameNotExistException e) {
                                String trainer = StringArgumentType.getString(context, "trainer");

                                context.getSource().getPlayer().sendMessage(
                                        Text.literal(String.format("Unknown trainer %s", trainer)).formatted(Formatting.RED));
                                CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                                CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                                return -1;
                            }
                        }));
    }
}
