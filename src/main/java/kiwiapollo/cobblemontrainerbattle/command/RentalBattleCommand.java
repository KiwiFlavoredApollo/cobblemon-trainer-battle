package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.executor.RentalBattleStarter;
import kiwiapollo.cobblemontrainerbattle.command.predicate.PlayerCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class RentalBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalBattleCommand() {
        super("rentalbattle");

        List<String> permissions = List.of(
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
        );

        this.requires(new PlayerCommandSourcePredicate(permissions.toArray(String[]::new)))
                .then(getSelectedTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSelectedTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandSourcePredicate(permission))
                .suggests((context, builder) -> {
                    TrainerStorage.getInstance().keySet().forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(new RentalBattleStarter.BetweenThisPlayerAndSelectedTrainer());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                .requires(new PlayerCommandSourcePredicate(permission))
                .executes(new RentalBattleStarter.BetweenThisPlayerAndRandomTrainer());
    }
}
