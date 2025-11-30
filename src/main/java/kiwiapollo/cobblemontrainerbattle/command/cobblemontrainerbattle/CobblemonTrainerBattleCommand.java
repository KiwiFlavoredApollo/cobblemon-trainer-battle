package kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.common.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.event.LegacyItemMigrator;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.MOD_ID);

        this.then(getExportCommand())
                .then(getMigrateCommand());
    }

    private LiteralArgumentBuilder<ServerCommandSource> getExportCommand() {
        String permission = String.format("%s.%s", getLiteral(), "export");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .executes(new ShowdownPokemonExporter()));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getMigrateCommand() {
        String permission = String.format("%s.%s", getLiteral(), "migrateitem");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("migrateitem")
                .requires(new MultiCommandSourcePredicate(permission))
                .executes(new LegacyItemMigrator());
    }
}
