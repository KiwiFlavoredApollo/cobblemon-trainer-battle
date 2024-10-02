package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.command.ServerCommandSource;

public class BattleTrainerFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public BattleTrainerFlatCommand() {
        super("battletrainerflat");

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                .then(getSpecificTrainerBattleCommand())
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(TrainerBattle::startRandomBattleWithFlatLevelAndFullHealth));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSpecificTrainerBattleCommand() {
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                .suggests((context, builder) -> {
                    CobblemonTrainerBattle.trainerFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(TrainerBattle::startTrainerBattleWithFlatLevelAndFullHealth);
    }
}
