package kiwiapollo.cobblemontrainerbattle.trainerbattle;

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
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerState;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.common.TrainerCondition;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattle {
    public static final int FLAT_LEVEL = 100;

    public static int startTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithStatusQuo(context, trainer);

        } catch (InvalidResourceStateException e) {
            if (e.getInvalidResourceState().equals(InvalidResourceState.UNREADABLE)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("An error occurred while reading %s", e.getResourcePath())));
            }
            return -1;
        }
    }


    public static int startRandomBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
         Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
         return startSpecificTrainerBattleWithStatusQuo(context, trainer);
    }

    public static int startSpecificTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertNotPlayerBusyWithAnotherPokemonBattle(context.getSource().getPlayer());
            assertSatisfiedTrainerCondition(context.getSource().getPlayer(), trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Trainer battle has started against %s", trainer.name)));
                CobblemonTrainerBattle.LOGGER.info(String.format("battletrainer: %s versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (UnsatisfiedTrainerConditionException e) {
            if (e.getUnsatisfiedCondition().equals(TrainerCondition.MINIMUM_PARTY_LEVEL)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Your Pokemons must be at or above level %d",
                                (Integer) e.getRequiredValue())).formatted(Formatting.RED));
            }

            if (e.getUnsatisfiedCondition().equals(TrainerCondition.MAXIMUM_PARTY_LEVEL)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Your Pokemons must be at or below level %d",
                                (Integer) e.getRequiredValue())).formatted(Formatting.RED));
            }

            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;

        } catch (InvalidPlayerStateException e) {
            if (e.getInvalidPlayerState().equals(InvalidPlayerState.EMPTY_POKEMON_PARTY)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("You have no Pokemon").formatted(Formatting.RED));
            }

            if (e.getInvalidPlayerState().equals(InvalidPlayerState.FAINTED_POKEMON_PARTY)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("Your Pokemons are all fainted").formatted(Formatting.RED));
            }

            if (e.getInvalidPlayerState().equals(InvalidPlayerState.POKEMON_PARTY_BELOW_RELATIVE_LEVEL_THRESHOLD)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Pokemon levels should be above %d",
                                TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)).formatted(Formatting.RED));
            }

            if (e.getInvalidPlayerState().equals(InvalidPlayerState.BUSY_WITH_ANOTHER_POKEMON_BATTLE)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("You cannot start trainer battle while on another").formatted(Formatting.RED));
            }

            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int startTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);

        } catch (InvalidResourceStateException e) {
            if (e.getInvalidResourceState().equals(InvalidResourceState.UNREADABLE)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("An error occurred while reading %s", e.getResourcePath())));
            }
            return -1;
        }
    }

    public static int startRandomBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
        return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);
    }

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotPlayerBusyWithAnotherPokemonBattle(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer()).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory()
                            .create(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory()
                            .create(trainer, FLAT_LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Flat level trainer battle has started against %s", trainer.name)));
                CobblemonTrainerBattle.LOGGER.info(String.format("battletrainerflat: %s versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (InvalidPlayerStateException e) {
            if (e.getInvalidPlayerState().equals(InvalidPlayerState.EMPTY_POKEMON_PARTY)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("You have no Pokemon").formatted(Formatting.RED));
            }

            if (e.getInvalidPlayerState().equals(InvalidPlayerState.BUSY_WITH_ANOTHER_POKEMON_BATTLE)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal("You cannot start trainer battle while on another").formatted(Formatting.RED));
            }

            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    private static void assertSatisfiedTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        assertSatisfiedMinimumLevelTrainerCondition(player, trainer);
        assertSatisfiedMaximumLevelTrainerCondition(player, trainer);
    }

    private static void assertSatisfiedMaximumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int maximumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();
            boolean isAtOrBelowPartyMaximumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= maximumPartyLevel);

            if (!isAtOrBelowPartyMaximumLevel) {
                throw new UnsatisfiedTrainerConditionException(
                        String.format("Player did not satisfy maximum level condition: %s, %s", player, trainer.name),
                        TrainerCondition.MAXIMUM_PARTY_LEVEL,
                        maximumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int minimumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();
            boolean isAtOrAbovePartyMinimumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= minimumPartyLevel);

            if (!isAtOrAbovePartyMinimumLevel) {
                throw new UnsatisfiedTrainerConditionException(
                        String.format("Player did not satisfy minimum level condition: %s, %s", player, trainer.name),
                        TrainerCondition.MINIMUM_PARTY_LEVEL,
                        minimumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has no Pokemon: %s", player.getGameProfile().getName()),
                    InvalidPlayerState.EMPTY_POKEMON_PARTY);
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()),
                    InvalidPlayerState.POKEMON_PARTY_BELOW_RELATIVE_LEVEL_THRESHOLD);
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()),
                    InvalidPlayerState.FAINTED_POKEMON_PARTY);
        }
    }

    public static void assertNotPlayerBusyWithAnotherPokemonBattle(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new InvalidPlayerStateException(
                    String.format("Player is busy with another Pokemon battle: %s",
                            player.getGameProfile().getName()),
                    InvalidPlayerState.BUSY_WITH_ANOTHER_POKEMON_BATTLE);
        }
    }
}
