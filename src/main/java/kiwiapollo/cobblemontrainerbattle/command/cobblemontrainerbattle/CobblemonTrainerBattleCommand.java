package kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.common.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.event.LegacyItemMigrator;
import net.minecraft.server.command.ServerCommandSource;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.MOD_ID);

        this.then(getMigrateCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getMigrateCommand() {
        String permission = String.format("%s.%s", getLiteral(), "migrateitem");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("migrateitem")
                .requires(new MultiCommandSourcePredicate(permission))
                .executes(new LegacyItemMigrator());
    }
}
