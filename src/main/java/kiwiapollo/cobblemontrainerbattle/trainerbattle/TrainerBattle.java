package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.PlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.VirtualTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.ResourceValidator;
import kiwiapollo.cobblemontrainerbattle.common.UnsatisfiedTrainerConditionExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainerBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSelectedTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            String resourcePath = StringArgumentType.getString(context, "trainer");
            new ResourceValidator().assertExistValidTrainerResource(resourcePath);

            Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath);
            return startTrainerBattleWithStatusQuo(context.getSource().getPlayer(), identifier);

        } catch (InvalidResourceStateException e) {
            String resourcePath = StringArgumentType.getString(context, "trainer");
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", resourcePath);
            context.getSource().getPlayer().sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading trainer file: %s", resourcePath));
            return 0;
        }
    }

    public static int startRandomTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
         Identifier trainerIdentifier = new RandomTrainerIdentifierFactory().create();
         return startTrainerBattleWithStatusQuo(context.getSource().getPlayer(), trainerIdentifier);
    }

    public static int startTrainerBattleWithStatusQuo(ServerPlayerEntity player, Identifier trainerIdentifier) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotFaintPlayerParty();
            playerValidator.assertPlayerNotBusyWithPokemonBattle();
            playerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold();
            playerValidator.assertSatisfiedTrainerCondition(trainerIdentifier);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().createWithStatusQuo(player)),
                    new BattleSide(new VirtualTrainerBattleActorFactory(player).createWithStatusQuo(trainerIdentifier)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                trainerBattles.put(player.getUuid(), pokemonBattle);
                String trainerName = Paths.get(trainerIdentifier.getPath())
                        .getFileName().toString().replace(".json", "");

                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.success", trainerName));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleCommand().getLiteral(), player.getGameProfile().getName(), trainerName));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (EmptyPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (FaintedPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;

        }catch (BelowRelativeLevelThresholdException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()));
            return 0;

        } catch (UnsatisfiedTrainerConditionException e) {
            player.sendMessage(new UnsatisfiedTrainerConditionExceptionMessageFactory().create(e).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Trainer condition not satisfied: %s, %s", e.getTrainerConditionType(), e.getRequiredValue()));
            return 0;
        }
    }

    public static int startSelectedTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            String resourcePath = StringArgumentType.getString(context, "trainer");
            new ResourceValidator().assertExistValidTrainerResource(resourcePath);

            Identifier identifier = new Identifier(CobblemonTrainerBattle.NAMESPACE, resourcePath);
            return startTrainerBattleWithFlatLevelAndFullHealth(context.getSource().getPlayer(), identifier);

        } catch (InvalidResourceStateException e) {
            String resourcePath = StringArgumentType.getString(context, "trainer");
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", resourcePath);
            context.getSource().getPlayer().sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading trainer file: %s", resourcePath));
            return 0;
        }
    }

    public static int startRandomTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        Identifier trainerIdentifier = new RandomTrainerIdentifierFactory().create();
        return startTrainerBattleWithFlatLevelAndFullHealth(context.getSource().getPlayer(), trainerIdentifier);
    }

    public static int startTrainerBattleWithFlatLevelAndFullHealth(ServerPlayerEntity player, Identifier trainerIdentifier) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertPlayerNotBusyWithPokemonBattle();

            Cobblemon.INSTANCE.getStorage().getParty(player).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().createWithFlatLevelFullHealth(player, FLAT_LEVEL)),
                    new BattleSide(new VirtualTrainerBattleActorFactory(player).createWithFlatLevelFullHealth(trainerIdentifier, FLAT_LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                trainerBattles.put(player.getUuid(), pokemonBattle);
                String trainerName = Paths.get(trainerIdentifier.getPath())
                        .getFileName().toString().replace(".json", "");

                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattleflat.success", trainerName));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleFlatCommand().getLiteral(), player.getGameProfile().getName(), trainerName));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (EmptyPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }
}
