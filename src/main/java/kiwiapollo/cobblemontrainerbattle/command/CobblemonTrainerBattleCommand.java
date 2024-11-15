package kiwiapollo.cobblemontrainerbattle.command;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.economy.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.parser.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parser.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonExporter;
import kiwiapollo.cobblemontrainerbattle.predicates.PlayerPartyNotEmptyPredicate;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.MOD_ID);

        List<String> permissions = List.of(
                String.format("%s.%s", getLiteral(), "reload"),
                String.format("%s.%s", getLiteral(), "export")
        );

        this.requires(new MultiCommandSourcePredicate(permissions.toArray(String[]::new)))
                .then(getReloadCommand())
                .then(getExportCommand())
                .then(getExportFlatCommand())
                .then(getExportRelativeCommand());
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

    private LiteralArgumentBuilder<ServerCommandSource> getExportFlatCommand() {
        String permission = String.format("%s.%s", getLiteral(), "export");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("exportflat")
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .then(RequiredArgumentBuilder.<ServerCommandSource, Integer>argument("level", IntegerArgumentType.integer(20, 100))
                                .executes(this::exportPlayerWithFlatLevel)));
    }

    private LiteralArgumentBuilder<ServerCommandSource> getExportRelativeCommand() {
        String permission = String.format("%s.%s", getLiteral(), "export");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("exportrelative")
                .requires(new MultiCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>argument("player", EntityArgumentType.player())
                        .executes(this::exportPlayerWithRelativeLevel));
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) {
        CobblemonTrainerBattle.config = ConfigLoader.load();
        CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
        CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
        return Command.SINGLE_SUCCESS;
    }

    private int exportPlayer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            PlayerPartyNotEmptyPredicate<ServerPlayerEntity> isPlayerPartyNotEmpty = new PlayerPartyNotEmptyPredicate<>();
            if (!isPlayerPartyNotEmpty.test(player)) {
                return 0;
            }

            List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                    .getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull).toList();

            List<ShowdownPokemon> showdownPokemons = pokemons.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String exportFileName = String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
            File exportFile = new File(ShowdownPokemonExporter.EXPORT_DIR, exportFileName);
            new ShowdownPokemonExporter().toJson(showdownPokemons, exportFile);

            context.getSource().sendMessage(Text.literal(String.format("Exported %s's Pokemon team", player.getGameProfile().getName())));
            CobblemonTrainerBattle.LOGGER.info("Successfully exported trainer file: {}", exportFile.getPath());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting trainer file");
            return 0;
        }
    }

    private int exportPlayerWithFlatLevel(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            PlayerPartyNotEmptyPredicate<ServerPlayerEntity> isPlayerPartyNotEmpty = new PlayerPartyNotEmptyPredicate<>();
            if (!isPlayerPartyNotEmpty.test(player)) {
                return 0;
            }

            int level = IntegerArgumentType.getInteger(context, "level");

            List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                    .getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull).toList();

            List<ShowdownPokemon> showdownPokemons = pokemons.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
            showdownPokemons.forEach(smogonPokemon -> smogonPokemon.level = level);

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String exportFileName = String.format("%s_flat_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
            File exportFile = new File(ShowdownPokemonExporter.EXPORT_DIR, exportFileName);
            new ShowdownPokemonExporter().toJson(showdownPokemons, exportFile);

            context.getSource().sendMessage(Text.literal(String.format("Exported %s's Pokemon team", player.getGameProfile().getName())));
            CobblemonTrainerBattle.LOGGER.info("Successfully exported trainer file: {}", exportFile.getPath());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting trainer file");
            return 0;
        }
    }

    private int exportPlayerWithRelativeLevel(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            PlayerPartyNotEmptyPredicate<ServerPlayerEntity> isPlayerPartyNotEmpty = new PlayerPartyNotEmptyPredicate<>();
            if (!isPlayerPartyNotEmpty.test(player)) {
                return 0;
            }

            List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                    .getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull).toList();

            List<ShowdownPokemon> showdownPokemons = pokemons.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
            showdownPokemons.forEach(smogonPokemon -> smogonPokemon.level = 0);

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String exportFileName = String.format("%s_relative_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
            File exportFile = new File(ShowdownPokemonExporter.EXPORT_DIR, exportFileName);
            new ShowdownPokemonExporter().toJson(showdownPokemons, exportFile);

            context.getSource().sendMessage(Text.literal(String.format("Exported %s's Pokemon team", player.getGameProfile().getName())));
            CobblemonTrainerBattle.LOGGER.info("Successfully exported trainer file: {}", exportFile.getPath());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting trainer file");
            return 0;
        }
    }
}
