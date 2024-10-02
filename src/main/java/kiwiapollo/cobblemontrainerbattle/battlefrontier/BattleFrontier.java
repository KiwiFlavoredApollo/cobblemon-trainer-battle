package kiwiapollo.cobblemontrainerbattle.battlefrontier;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.PokemonStats;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.BattleFrontierPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.BattleFrontierNameTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.ExecuteCommandFailedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.IllegalPlayerStateException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidBattleSessionStateException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.ThreePokemonTotalRandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BattleFrontier {
    public static final int LEVEL = 100;
    public static Map<UUID, BattleFrontierSession> SESSIONS = new HashMap<>();

    public static void startSession(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleFrontierSession());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is started"));
            showPartyPokemons(context);
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Started Battle Frontier session",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    public static void stopSession(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is stopped"));
            CobblemonTrainerBattle.LOGGER.info(String.format("%s: Stopped Battle Frontier session",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    public static void startBattle(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());

            Trainer trainer = new ThreePokemonTotalRandomTrainerFactory().create(context.getSource().getPlayer());
            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new BattleFrontierPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new BattleFrontierNameTrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);
                UUID playerUuid = context.getSource().getPlayer().getUuid();
                BattleFrontier.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Battle Frontier Pokemon Battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();

        } catch (IllegalPlayerStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    public static void tradePokemon(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotPlayerDefeated(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context.getSource().getPlayer());
            assertNotPlayerTradedPokemon(context.getSource().getPlayer());

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.defeatedTrainers.get(session.defeatedTrainers.size() - 1);
            Pokemon trainerPokemon = lastDefeatedTrainer.pokemons.get(trainerslot - 1);
            Pokemon playerPokemon = session.partyPokemons.get(playerslot - 1);

            session.partyPokemons = new ArrayList<>(session.partyPokemons);
            session.partyPokemons.set(playerslot - 1, trainerPokemon.clone(true, true));
            session.isTradedPokemon = true;

            Text pokemonTradeMessage = Text.literal("Traded ")
                    .append(playerPokemon.getDisplayName())
                    .append(Text.literal(" for "))
                    .append(trainerPokemon.getDisplayName())
                    .formatted(Formatting.YELLOW);

            context.getSource().getPlayer().sendMessage(pokemonTradeMessage);
            CobblemonTrainerBattle.LOGGER.info(pokemonTradeMessage.getString());

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();

        } catch (IllegalPlayerStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    public static void showTradeablePokemons(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context.getSource().getPlayer());

            BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.defeatedTrainers.get(session.defeatedTrainers.size() - 1);
            printPokemons(context, lastDefeatedTrainer.pokemons);

        } catch (InvalidBattleSessionStateException e) {
            throw new ExecuteCommandFailedException();

        } catch (IllegalPlayerStateException e) {
            throw new ExecuteCommandFailedException();
        }
    }

    public static void showPartyPokemons(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).partyPokemons);

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    public static void rerollPokemons(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertNotExistDefeatedTrainers(context.getSource().getPlayer());

            BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());

            session.partyPokemons = new RandomPartyPokemonsFactory().create();
            context.getSource().getPlayer().sendMessage(Text.literal("Rerolled Pokemons"));
            showPartyPokemons(context);

        } catch (InvalidBattleSessionStateException e) {
            throw new ExecuteCommandFailedException();

        } catch (IllegalPlayerStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You cannot reroll Pokemons after battling trainers").formatted(Formatting.RED));
            throw new ExecuteCommandFailedException();
        }
    }

    public static void showWinningStreak(CommandContext<ServerCommandSource> context) throws ExecuteCommandFailedException {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            int winningStreak = session.defeatedTrainers.size();
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("Winning streak: %d", winningStreak)));

        } catch (InvalidBattleSessionStateException e) {
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            throw new ExecuteCommandFailedException();
        }
    }

    private static void assertNotPlayerDefeated(ServerPlayerEntity player) throws IllegalPlayerStateException {
        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new IllegalPlayerStateException(
                    String.format("Player is defeated: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertExistValidSession(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (!isExistValidSession(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Valid battle session does not exists: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertNotExistValidSession(ServerPlayerEntity player)
            throws InvalidBattleSessionStateException {
        if (isExistValidSession(player)) {
            throw new InvalidBattleSessionStateException(
                    String.format("Valid battle session exists: %s", player.getGameProfile().getName()));
        }
    }

    private static boolean isExistValidSession(ServerPlayerEntity player) {
        if (!SESSIONS.containsKey(player.getUuid())) {
            return false;
        }

        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    private static void assertNotPlayerTradedPokemon(ServerPlayerEntity player)
            throws IllegalPlayerStateException {
        if (SESSIONS.get(player.getUuid()).isTradedPokemon) {
            throw new IllegalPlayerStateException(
                    String.format("Player already traded a Pokemon: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertExistDefeatedTrainer(ServerPlayerEntity player)
            throws IllegalPlayerStateException {
        if (!isExistDefeatedTrainers(player)) {
            throw new IllegalPlayerStateException(
                    String.format("Player has no defeated trainers: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertNotExistDefeatedTrainers(ServerPlayerEntity player)
            throws IllegalPlayerStateException {
        if (isExistDefeatedTrainers(player)) {
            throw new IllegalPlayerStateException(
                    String.format("Player has defeated trainers: %s", player.getGameProfile().getName()));
        }
    }

    private static boolean isExistDefeatedTrainers(ServerPlayerEntity player) {
        return !SESSIONS.get(player.getUuid()).defeatedTrainers.isEmpty();
    }

    private static void printPokemons(CommandContext<ServerCommandSource> context, List<Pokemon> pokemons) {
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            context.getSource().getPlayer().sendMessage(
                    Text.literal("[" + (i + 1) + "] ").append(pokemon.getDisplayName()).formatted(Formatting.YELLOW));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Ability ").append(Text.translatable(pokemon.getAbility().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("Nature ").append(Text.translatable(pokemon.getNature().getDisplayName())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("MoveSet ").append(getPokemonMoveSetReport(pokemon.getMoveSet())));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("EVs ").append(Text.literal(getPokemonStatsReport(pokemon.getEvs()))));
            context.getSource().getPlayer().sendMessage(
                    Text.literal("IVs ").append(Text.literal(getPokemonStatsReport(pokemon.getIvs()))));
        }
    }

    private static Text getPokemonMoveSetReport(MoveSet moveSet) {
        MutableText moveSetReport = Text.literal("");
        for (Move move : moveSet.getMoves()) {
            if (moveSetReport.equals(Text.literal(""))) {
                moveSetReport.append(move.getDisplayName());
            } else {
                moveSetReport.append(Text.literal(" / ")).append(move.getDisplayName());
            }
        }
        return moveSetReport;
    }

    private static String getPokemonStatsReport(PokemonStats stats) {
        return String.format("HP %d / ATK %d / DEF %d / SPA %d / SPD %d / SPE %d",
                stats.get(Stats.HP), stats.get(Stats.ATTACK), stats.get(Stats.DEFENCE),
                stats.get(Stats.SPECIAL_ATTACK), stats.get(Stats.SPECIAL_DEFENCE), stats.get(Stats.SPEED));
    };

    private static void assertNotExistPlayerParticipatingPokemonBattle(ServerPlayerEntity player)
            throws IllegalPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new IllegalPlayerStateException(
                    String.format("Already participating in another Pokemon battle: %s",
                            player.getGameProfile().getName()));
        }
    }
}
