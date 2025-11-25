package kiwiapollo.cobblemontrainerbattle.command.rentalbattle;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.common.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.command.common.TrainerSuggestionProvider;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import java.util.List;

public class RentalBattleOtherCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalBattleOtherCommand() {
        super("rentalbattleother");

        List<String> permissions = List.of(
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
        );

        this.requires(new MultiCommandSourcePredicate(permissions))
                .then(getSelectedTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSelectedTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, Identifier>argument("trainer", IdentifierArgumentType.identifier())
                        .suggests(new TrainerSuggestionProvider())
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
