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
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.CommandConditionType;
import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;
import kiwiapollo.cobblemontrainerbattle.common.TrainerCondition;
import kiwiapollo.cobblemontrainerbattle.exceptions.CommandConditionNotSatisfiedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.UnsatisfiedTrainerConditionException;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattle {
    public static final int FLAT_LEVEL = 100;

    public static int startTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            assertExistTrainerResource(trainerResourcePath);
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithStatusQuo(context, trainer);

        } catch (InvalidResourceStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidResourceStateErrorMessage(e)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
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
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());
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
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (UnsatisfiedTrainerConditionException e) {
            Text message = switch (e.getUnsatisfiedCondition()) {
                case MAXIMUM_PARTY_LEVEL ->
                        Text.translatable("command.cobblemontrainerbattle.maximum_party_level",
                                e.getRequiredValue());
                case MINIMUM_PARTY_LEVEL ->
                        Text.translatable("command.cobblemontrainerbattle.minimum_party_level",
                                e.getRequiredValue());
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.empty_player_party");
                case FAINTED_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.fainted_player_party");
                case BELOW_RELATIVE_LEVEL_THRESHOLD ->
                        Text.translatable("command.cobblemontrainerbattle.below_relative_level_threshold");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.minimum_party_level");
                default -> Text.literal("");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int startTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            assertExistTrainerResource(trainerResourcePath);
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);

        } catch (InvalidResourceStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(getInvalidResourceStateErrorMessage(e)).formatted(Formatting.RED));
            return 0;
        }
    }

    public static int startRandomBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
        return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);
    }

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotPlayerBusyWithPokemonBattle(context.getSource().getPlayer());

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
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleFlatCommand().getLiteral(),
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (CommandConditionNotSatisfiedException e) {
            Text message = switch (e.getCommandConditionType()) {
                case EMPTY_PLAYER_PARTY ->
                        Text.translatable("command.cobblemontrainerbattle.empty_player_party");
                case BUSY_WITH_POKEMON_BATTLE ->
                        Text.translatable("command.cobblemontrainerbattle.minimum_party_level");
                default -> Text.literal("");
            };
            context.getSource().getPlayer().sendMessage(message.copy().formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    private static void assertExistTrainerResource(String trainerResourcePath) throws InvalidResourceStateException {
        if (!CobblemonTrainerBattle.trainerFiles.containsKey(trainerResourcePath)) {
            throw new InvalidResourceStateException(
                    String.format("Trainer file is not loaded: %s", trainerResourcePath),
                    InvalidResourceState.UNKNOWN_RESOURCE,
                    trainerResourcePath
            );
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
                    .get(trainer.name).configuration
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
                    .get(trainer.name).configuration
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

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new CommandConditionNotSatisfiedException(
                    String.format("Player has no Pokemon: %s", player.getGameProfile().getName()),
                    CommandConditionType.EMPTY_PLAYER_PARTY
            );
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new CommandConditionNotSatisfiedException(
                    String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()),
                    CommandConditionType.BELOW_RELATIVE_LEVEL_THRESHOLD
            );
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new CommandConditionNotSatisfiedException(
                    String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()),
                    CommandConditionType.FAINTED_PLAYER_PARTY
            );
        }
    }

    public static void assertNotPlayerBusyWithPokemonBattle(ServerPlayerEntity player)
            throws CommandConditionNotSatisfiedException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new CommandConditionNotSatisfiedException(
                    String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()),
                    CommandConditionType.BUSY_WITH_POKEMON_BATTLE
            );
        }
    }

    private static String getInvalidResourceStateErrorMessage(InvalidResourceStateException e) {
        if (e.getInvalidResourceState().equals(InvalidResourceState.UNREADABLE_RESOURCE)) {
            return String.format("An error occurred while reading %s", e.getResourcePath());
        }

        if (e.getInvalidResourceState().equals(InvalidResourceState.CONTAINS_INVALID_VALUE)) {
            return String.format("Invalid values found in %s", e.getResourcePath());
        }

        throw new RuntimeException(e);
    }
}
