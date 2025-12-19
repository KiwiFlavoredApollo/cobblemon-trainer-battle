package kiwiapollo.cobblemontrainerbattle.command.exportpokemon;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.common.PlayerCommandSourcePredicate;
import net.minecraft.server.command.ServerCommandSource;

public class ExportPokemonCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public ExportPokemonCommand() {
        super("exportpokemon");

        String permission = String.format("%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral());
        this.requires(new PlayerCommandSourcePredicate(permission))
                .executes(new ShowdownTeamExporter.ThisPlayer());
    }
}
