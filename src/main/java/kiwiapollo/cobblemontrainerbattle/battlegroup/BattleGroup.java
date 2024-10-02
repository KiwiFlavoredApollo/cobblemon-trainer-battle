package kiwiapollo.cobblemontrainerbattle.battlegroup;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.BattleFrontierPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.BattleFrontierNameTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class BattleGroup {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, BattleGroupSession> SESSIONS = new HashMap<>();

    public static void startSession(CommandContext<ServerCommandSource> context) throws ExecuteCommandFaildException {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            String groupFilePath = StringArgumentType.getString(context, "group");
            BattleGroup.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleGroupSession(groupFilePath));

            context.getSource().getPlayer().sendMessage(Text.literal("battle group session is started"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started battle group session",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (ValidBattleFrontierSessionExistException e) {
            printValidBattleGroupSessionExistErrorMessage(context);
            throw new ExecuteCommandFaildException();
        }
    }

    public static void stopSession(CommandContext<ServerCommandSource> context) throws ExecuteCommandFaildException {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleGroup.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("battle group session is stopped"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped battle group session",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            printValidBattleGroupSessionNotExistErrorMessage(context);
            throw new ExecuteCommandFaildException();
        }
    }

    public static void battleGroupWithStatusQuo(CommandContext<ServerCommandSource> context)
            throws ExecuteCommandFaildException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(context.getSource().getPlayer());
            assertExistNextTrainer(context.getSource().getPlayer());

            Trainer trainer = createNextTrainer(context.getSource().getPlayer());
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                CobblemonTrainerBattle.trainerBattles.put(playerUuid, pokemonBattle);
                BattleGroup.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getPlayer().sendMessage(
                        Text.literal("battlegroup Pokemon battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

        } catch (ValidBattleFrontierSessionNotExistException e) {
            printValidBattleGroupSessionNotExistErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (BattleFrontierDefeatedPlayerException e) {
            printPlayerDefeatedErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (PlayerParticipatingPokemonBattleExistException e) {
            printPlayerParticipatingPokemonBattleExistErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (EmptyPlayerPartyException e) {
            printEmptyPlayerPartyErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (FaintPlayerPartyException e) {
            printFaintedPlayerPartyErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (PlayerPartyBelowRelativeLevelThresholdException e) {
            printPlayerPartyBelowRelativeLevelThresholdErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (NextTrainerNotExistException e) {
            printNextTrainerNotExistErrorMessage(context);
            throw new ExecuteCommandFaildException();
        }
    }

    public static void battleGroupWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context)
            throws ExecuteCommandFaildException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertExistNextTrainer(context.getSource().getPlayer());

            Trainer trainer = createNextTrainer(context.getSource().getPlayer());
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, FLAT_LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                CobblemonTrainerBattle.trainerBattles.put(playerUuid, pokemonBattle);
                BattleGroup.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getPlayer().sendMessage(
                        Text.literal("battlegroup Pokemon battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

        } catch (ValidBattleFrontierSessionNotExistException e) {
            printValidBattleGroupSessionNotExistErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (BattleFrontierDefeatedPlayerException e) {
            printPlayerDefeatedErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (PlayerParticipatingPokemonBattleExistException e) {
            printPlayerParticipatingPokemonBattleExistErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (EmptyPlayerPartyException e) {
            printEmptyPlayerPartyErrorMessage(context);
            throw new ExecuteCommandFaildException();

        } catch (NextTrainerNotExistException e) {
            printNextTrainerNotExistErrorMessage(context);
            throw new ExecuteCommandFaildException();
        }
    }

    private static Trainer createNextTrainer(ServerPlayerEntity player) throws NextTrainerNotExistException {
        try {
            BattleGroupSession session = SESSIONS.get(player.getUuid());
            int defeatedTrainersCount = session.defeatedTrainers.size();
            String nextTrainerResourcePath = CobblemonTrainerBattle.groupFiles
                    .get(session.groupResourcePath).configuration
                    .get("trainers").getAsJsonArray()
                    .get(defeatedTrainersCount).getAsString();
            return new SpecificTrainerFactory().create(player, nextTrainerResourcePath);

        } catch (CreateTrainerFailedException e) {
            throw new NextTrainerNotExistException();
        }
    }

    private static void printValidBattleGroupSessionNotExistErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal("You do not have valid battle group session").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(String.format("%s: Valid Battle Group session does not exists",
                context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printValidBattleGroupSessionExistErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal("battle group session already exists").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(String.format("%s: Valid Battle Group session exists",
                context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printPlayerDefeatedErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal("You are defeated. Please create another battle group session").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(String.format("%s: battle group session expired due to defeat",
                context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printPlayerParticipatingPokemonBattleExistErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal("You cannot start Pokemon battle while on another").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(String.format("%s: Already participating in another Pokemon battle",
                context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printEmptyPlayerPartyErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(Text.literal("You have no Pokemon"));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(
                String.format("%s: Player has no Pokemon", context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printPlayerPartyBelowRelativeLevelThresholdErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal(String.format("You must have at least one Pokemon at or above level %d",
                        TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)).formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(
                String.format("%s: Pokemons under leveled", context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printFaintedPlayerPartyErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(Text.literal("Your Pokemons are all fainted").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(
                String.format("%s: Pokemons are all fainted", context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void printNextTrainerNotExistErrorMessage(CommandContext<ServerCommandSource> context) {
        context.getSource().getPlayer().sendMessage(
                Text.literal("You have defeated all trainers").formatted(Formatting.RED));
        CobblemonTrainerBattle.LOGGER.error("An error occurred while executing battlegroup command");
        CobblemonTrainerBattle.LOGGER.error(
                String.format("%s: Player has defeated all trainers", context.getSource().getPlayer().getGameProfile().getName()));
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player) throws BattleFrontierDefeatedPlayerException {
        BattleGroupSession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new BattleFrontierDefeatedPlayerException();
        }
    }

    private static void assertExistValidSession(ServerPlayerEntity player)
            throws ValidBattleFrontierSessionNotExistException {
        if (!isExistValidSession(player)) {
            throw new ValidBattleFrontierSessionNotExistException();
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws ValidBattleFrontierSessionExistException {
        if (isExistValidSession(player)) {
            throw new ValidBattleFrontierSessionExistException();
        }
    }

    private static boolean isExistValidSession(ServerPlayerEntity player) {
        if (!SESSIONS.containsKey(player.getUuid())) {
            return false;
        }

        BattleGroupSession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    public static void assertNotExistPlayerParticipatingPokemonBattle(ServerPlayerEntity player)
            throws PlayerParticipatingPokemonBattleExistException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new PlayerParticipatingPokemonBattleExistException();
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

    private static void assertExistNextTrainer(ServerPlayerEntity player) throws NextTrainerNotExistException {
        try {
            BattleGroupSession session = SESSIONS.get(player.getUuid());
            int defeatedTrainersCount = session.defeatedTrainers.size();
            GroupFile groupFile = CobblemonTrainerBattle.groupFiles.get(session.groupResourcePath);
            String nextTrainerResourcePath = groupFile.configuration
                    .get("trainers").getAsJsonArray()
                    .get(defeatedTrainersCount).getAsString();

        } catch (NullPointerException | IllegalStateException
                 | UnsupportedOperationException | IndexOutOfBoundsException e) {
            throw new NextTrainerNotExistException();
        }
    }
}
