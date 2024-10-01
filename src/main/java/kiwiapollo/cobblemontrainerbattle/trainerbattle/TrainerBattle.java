package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthNameTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
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
    public static final int LEVEL = 100;

    public static void battleWithStatusQuo(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertSatisfiedTrainerCondition(context.getSource().getPlayer(), trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Status Quo Pokemon Battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("You have no Pokemon").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Player has no Pokemon",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (PlayerPartyBelowRelativeLevelThresholdException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("You must have at least one Pokemon at or above level %d",
                            TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Pokemons under leveled",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (FaintPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Your Pokemons are all fainted").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Pokemons are all fainted",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (PlayerParticipatingPokemonBattleExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You cannot start Pokemon battle while on another").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s: Already participating in another Pokemon battle",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (TrainerConditionNotSatisfiedException e) {
            context.getSource().getPlayer().sendMessage(Text.literal(e.message).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s: Player does not satisfy trainer condition",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    public static void battleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertSatisfiedTrainerCondition(context.getSource().getPlayer(), trainer);

            Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer()).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory()
                            .create(context.getSource().getPlayer(), LEVEL)),
                    new BattleSide(new FlatLevelFullHealthNameTrainerBattleActorFactory()
                            .create(trainer, LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Flat Level Full Health Pokemon Battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("You have no Pokemon"));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Player has no Pokemon",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (PlayerParticipatingPokemonBattleExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You cannot start Pokemon battle while on another"));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s: Already participating in another Pokemon battle",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (TrainerConditionNotSatisfiedException e) {
            context.getSource().getPlayer().sendMessage(Text.literal(e.message).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s: Player does not satisfy trainer condition",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    private static void assertSatisfiedTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionNotSatisfiedException {
        assertSatisfiedMinimumLevelTrainerCondition(player, trainer);
        assertSatisfiedMaximumLevelTrainerCondition(player, trainer);
    }

    private static void assertSatisfiedMaximumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionNotSatisfiedException {
        try {
            assertExistPartyMaximumLevelCondition(trainer);

            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int partyMaximumLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();
            boolean isAtOrBelowPartyMaximumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= partyMaximumLevel);

            if (!isAtOrBelowPartyMaximumLevel) {
                throw new TrainerConditionNotSatisfiedException(
                        String.format("Your Pokemons should be at or below level %d to battle %s",
                                partyMaximumLevel, trainer.name));
            }

        } catch (TrainerConditionNotExistException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionNotSatisfiedException {
        try {
            assertExistPartyMinimumLevelCondition(trainer);

            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int partyMinimumLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();
            boolean isAtOrAbovePartyMinimumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= partyMinimumLevel);

            if (!isAtOrAbovePartyMinimumLevel) {
                throw new TrainerConditionNotSatisfiedException(
                        String.format("Your Pokemons should be at or above level %d to battle %s",
                                partyMinimumLevel, trainer.name));
            }

        } catch (TrainerConditionNotExistException ignored) {

        }
    }

    private static void assertExistPartyMinimumLevelCondition(Trainer trainer) throws TrainerConditionNotExistException {
        try {
            CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException e) {
            throw new TrainerConditionNotExistException();
        }
    }

    private static void assertExistPartyMaximumLevelCondition(Trainer trainer) throws TrainerConditionNotExistException {
        try {
            CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException e) {
            throw new TrainerConditionNotExistException();
        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new EmptyPlayerPartyException();
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws PlayerPartyBelowRelativeLevelThresholdException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new PlayerPartyBelowRelativeLevelThresholdException();
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player) throws FaintPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new FaintPlayerPartyException();
        }
    }

    public static void assertNotExistPlayerParticipatingPokemonBattle(ServerPlayerEntity player)
            throws PlayerParticipatingPokemonBattleExistException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new PlayerParticipatingPokemonBattleExistException();
        }
    }
}
