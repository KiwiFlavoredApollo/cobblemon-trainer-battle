package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.predicate.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.config.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.command.executor.ShowdownPokemonExporter;
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

        this.requires(new MultiCommandSourcePredicate(permissions.toArray(String[]::new)))
                .then(getReloadCommand())
                .then(getExportCommand());
    }

    private LiteralArgumentBuilder<ServerCommandSource> getReloadCommand() {
        String permission = String.format("%s.%s", getLiteral(), "reload");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                .requires(new MultiCommandSourcePredicate(permission))
                .executes(this::reloadConfig);
    }

    private LiteralArgumentBuilder<ServerCommandSource> getExportCommand() {
        String permission = String.format("%s.%s", getLiteral(), "export");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .executes(new ShowdownPokemonExporter()));
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) {
        CobblemonTrainerBattle.config = new ConfigLoader().load();
        CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
        return Command.SINGLE_SUCCESS;
    }
}
