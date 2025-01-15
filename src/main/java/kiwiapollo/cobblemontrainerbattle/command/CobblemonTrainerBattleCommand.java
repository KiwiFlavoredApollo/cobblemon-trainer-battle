package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.FlatLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.PlayerPartyNotEmptyPredicate;
import kiwiapollo.cobblemontrainerbattle.parser.config.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.parser.exporter.FlatShowdownPokemonExporter;
import kiwiapollo.cobblemontrainerbattle.parser.exporter.NormalShowdownPokemonExporter;
import kiwiapollo.cobblemontrainerbattle.parser.exporter.RelativeShowdownPokemonExporter;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
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
                        .executes(this::exportPlayer));
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) {
        CobblemonTrainerBattle.config = new ConfigLoader().load();
        CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
        return Command.SINGLE_SUCCESS;
    }

    private int exportPlayer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            MessagePredicate<PlayerBattleParticipant> predicate = new PlayerPartyNotEmptyPredicate();
            if (!predicate.test(new NormalLevelPlayer(player))) {
                player.sendMessage(predicate.getErrorMessage());
                return 0;
            }

            new NormalShowdownPokemonExporter(player).toJson();

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("Error occurred while exporting trainer file");
            return 0;
        }
    }
}
