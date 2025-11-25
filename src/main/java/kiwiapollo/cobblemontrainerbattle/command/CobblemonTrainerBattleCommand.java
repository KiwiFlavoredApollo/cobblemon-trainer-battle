package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.MOD_ID);

        List<String> permissions = List.of(
                String.format("%s.%s", getLiteral(), "reload"),
                String.format("%s.%s", getLiteral(), "export")
        );

        this.requires(new MultiCommandSourcePredicate(permissions))
                .then(getExportCommand());
    }

    private LiteralArgumentBuilder<ServerCommandSource> getExportCommand() {
        String permission = String.format("%s.%s", getLiteral(), "export");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .executes(new ShowdownPokemonExporter()));
    }
}
