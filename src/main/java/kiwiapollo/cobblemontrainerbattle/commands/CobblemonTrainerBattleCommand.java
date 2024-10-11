package kiwiapollo.cobblemontrainerbattle.commands;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.economies.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.parsers.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemonExporter;
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
        super(CobblemonTrainerBattle.NAMESPACE);

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload"), String.format("%s.%s", getLiteral(), "export")))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload")))
                        .executes(this::reloadConfig))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "export")))
                        .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>
                                        argument("player", EntityArgumentType.player())
                                .executes(this::exportPlayerToTrainer)));
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) {
        CobblemonTrainerBattle.config = ConfigLoader.load();
        CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
        CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
        return Command.SINGLE_SUCCESS;
    }

    private int exportPlayerToTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            new PlayerValidator(player).assertNotEmptyPlayerParty();

            List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                    .getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull).toList();

            List<SmogonPokemon> smogonPokemons = pokemons.stream().map(new CobblemonPokemonParser()::toSmogonPokemon).toList();

            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String exportFileName = String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
            File exportFile = new File(SmogonPokemonExporter.EXPORT_DIR, exportFileName);
            new SmogonPokemonExporter().toJson(smogonPokemons, exportFile);

            context.getSource().sendMessage(Text.literal(String.format("Exported %s's Pokemon team", player.getGameProfile().getName())));
            CobblemonTrainerBattle.LOGGER.info(String.format("Successfully exported trainer file: %s", exportFile.getPath()));

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;

        } catch (EmptyPlayerPartyException e) {
            CobblemonTrainerBattle.LOGGER.error("Player has no Pokemon");
            return 0;

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting trainer file");
            return 0;
        }
    }
}
