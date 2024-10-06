package kiwiapollo.cobblemontrainerbattle.npc;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedFlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerStateType;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionType;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidPlayerStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerConditionUnsatisfiedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SpecificTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerFileParser;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.stream.Stream;

public class EntityBackedTrainerBattle {
    public static final int FLAT_LEVEL = 100;

    public static int startSpecificTrainerBattleWithStatusQuo(ServerPlayerEntity player, Trainer trainer, TrainerEntity trainerEntity) {
        try {
            assertNotEmptyPlayerParty(player);
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(player);
            assertNotFaintPlayerParty(player);
            assertNotPlayerBusyWithPokemonBattle(player);
            assertSatisfiedTrainerCondition(player, trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(player)),
                    new BattleSide(new EntityBackedTrainerBattleActorFactory().create(trainer, trainerEntity)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(player.getUuid(), pokemonBattle);

                player.sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.trainerbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleCommand().getLiteral(),
                        player.getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (TrainerConditionUnsatisfiedException e) {
            Text message = switch (e.getTrainerConditionType()) {
                case MAXIMUM_PARTY_LEVEL ->
                        Text.translatable("command.cobblemontrainerbattle.trainer.maximum_party_level",
                                e.getRequiredValue());
                case MINIMUM_PARTY_LEVEL ->
                        Text.translatable("command.cobblemontrainerbattle.trainer.minimum_party_level",
                                e.getRequiredValue());
            };
            player.sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
                case FAINTED_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party");
                case BELOW_RELATIVE_LEVEL_THRESHOLD ->
                        Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
                default -> throw new RuntimeException(e);
            };
            player.sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(ServerPlayerEntity player, Trainer trainer, TrainerEntity trainerEntity) {
        try {
            assertNotEmptyPlayerParty(player);
            assertNotPlayerBusyWithPokemonBattle(player);

            Cobblemon.INSTANCE.getStorage().getParty(player).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory()
                            .create(player, FLAT_LEVEL)),
                    new BattleSide(new EntityBackedFlatLevelFullHealthTrainerBattleActorFactory()
                            .create(trainer, FLAT_LEVEL, trainerEntity)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(player.getUuid(), pokemonBattle);

                player.sendMessage(
                        Text.translatable("command.cobblemontrainerbattle.trainerbattleflat.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleFlatCommand().getLiteral(),
                        player.getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            Text message = switch (e.getInvalidPlayerStateType()) {
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
                default -> throw new RuntimeException(e);
            };
            player.sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void assertSatisfiedTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionUnsatisfiedException {
        assertSatisfiedMinimumLevelTrainerCondition(player, trainer);
        assertSatisfiedMaximumLevelTrainerCondition(player, trainer);
    }

    private static void assertSatisfiedMaximumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionUnsatisfiedException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int maximumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(trainer.name).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();
            boolean isAtOrBelowPartyMaximumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= maximumPartyLevel);

            if (!isAtOrBelowPartyMaximumLevel) {
                throw new TrainerConditionUnsatisfiedException(
                        String.format("Player did not satisfy maximum level condition: %s, %s", player, trainer.name),
                        TrainerConditionType.MAXIMUM_PARTY_LEVEL,
                        maximumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionUnsatisfiedException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int minimumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(trainer.name).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();
            boolean isAtOrAbovePartyMinimumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= minimumPartyLevel);

            if (!isAtOrAbovePartyMinimumLevel) {
                throw new TrainerConditionUnsatisfiedException(
                        String.format("Player did not satisfy minimum level condition: %s, %s", player, trainer.name),
                        TrainerConditionType.MINIMUM_PARTY_LEVEL,
                        minimumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has no Pokemon: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.EMPTY_PLAYER_PARTY
            );
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.BELOW_RELATIVE_LEVEL_THRESHOLD
            );
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.FAINTED_PLAYER_PARTY
            );
        }
    }

    public static void assertNotPlayerBusyWithPokemonBattle(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new InvalidPlayerStateException(
                    String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()),
                    InvalidPlayerStateType.BUSY_WITH_POKEMON_BATTLE
            );
        }
    }
}
