package kiwiapollo.cobblemontrainerbattle.command.exportpokemon;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.common.MultiCommandSourcePredicate;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class ExportPokemonOtherCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public ExportPokemonOtherCommand() {
        super("exportpokemonother");

        String permission = String.format("%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral());
        this.requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .executes(new ShowdownTeamExporter.OtherPlayer()));
    }
}
