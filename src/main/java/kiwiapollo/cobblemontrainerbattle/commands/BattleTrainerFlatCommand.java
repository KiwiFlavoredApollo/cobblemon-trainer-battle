package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.ExecuteCommandFailedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
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
                .then(getSpecificTrainerBattleCommand())
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(context -> {
                            try {
                                Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
                                TrainerBattle.battleWithFlatLevelAndFullHealth(context, trainer);
                                return Command.SINGLE_SUCCESS;

                            } catch (ExecuteCommandFailedException e) {
                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSpecificTrainerBattleCommand() {
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                .suggests((context, builder) -> {
                    CobblemonTrainerBattle.trainerFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(context -> {
                    try {
                        String trainerResourcePath = StringArgumentType.getString(context, "trainer");
                        Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
                        TrainerBattle.battleWithFlatLevelAndFullHealth(context, trainer);
                        return Command.SINGLE_SUCCESS;

                    } catch (CreateTrainerFailedException e) {
                        String trainer = StringArgumentType.getString(context, "trainer");
                        context.getSource().getPlayer().sendMessage(
                                Text.literal(String.format("Unknown trainer %s", trainer)).formatted(Formatting.RED));
                        CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
                        CobblemonTrainerBattle.LOGGER.error(String.format("%s: Unknown trainer", trainer));

                        return -1;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }
}
