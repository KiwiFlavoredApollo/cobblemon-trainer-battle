package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BattleTrainerFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public BattleTrainerFlatCommand() {
        super("battletrainerflat");

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                .then(getRadicalRedTrainerBattleFlatCommand())
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(context -> {
                            TrainerBattle.battleWithFlatLevelAndFullHealth(context, new RandomTrainerFactory().create(context.getSource().getPlayer()));
                            return Command.SINGLE_SUCCESS;
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRadicalRedTrainerBattleFlatCommand() {
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                .suggests((context, builder) -> {
                    CobblemonTrainerBattle.trainerFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(context -> {
                    try {
                        String trainer = StringArgumentType.getString(context, "trainer");
                        TrainerBattle.battleWithFlatLevelAndFullHealth(context,
                                new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainer));
                        return Command.SINGLE_SUCCESS;

                    } catch (CreateTrainerFailedException e) {
                        String trainer = StringArgumentType.getString(context, "trainer");
                        context.getSource().getPlayer().sendMessage(
                                Text.literal(String.format("Unknown trainer %s", trainer)).formatted(Formatting.RED));
                        CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                        CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                        return -1;
                    }
                });
    }
}
