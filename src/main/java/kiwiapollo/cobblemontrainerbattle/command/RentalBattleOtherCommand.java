package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.executor.RentalBattleStarter;
import kiwiapollo.cobblemontrainerbattle.command.predicate.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class RentalBattleOtherCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalBattleOtherCommand() {
        super("trainerbattleother");

        List<String> permissions = List.of(
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
        );

        this.requires(new MultiCommandSourcePredicate(permissions.toArray(String[]::new)))
                .then(getSelectedTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSelectedTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            TrainerStorage.getInstance().keySet().stream().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(new RentalBattleStarter.BetweenOtherPlayerAndSelectedTrainer()));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                .requires(new MultiCommandSourcePredicate(permission))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .executes(new RentalBattleStarter.BetweenOtherPlayerAndRandomTrainer()));
    }
}
