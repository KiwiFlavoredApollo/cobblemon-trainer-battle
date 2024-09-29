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
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.RandomTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
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
    public static Map<UUID, BattleFrontierSession> SESSIONS = new HashMap<>();

    public static void start(CommandContext<ServerCommandSource> context) {
        try {
            assertNotExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.put(context.getSource().getPlayer().getUuid(), new BattleFrontierSession());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is created"));
            context.getSource().getPlayer().sendMessage(Text.literal("Please select starting Pokemons"));

        } catch (ValidBattleFrontierSessionExistException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session already exists"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session exists",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    public static void stop(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());

            BattleFrontier.SESSIONS.remove(context.getSource().getPlayer().getUuid());

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier session is removed"));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You do not have active Battle Frontier session"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session does not exists",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    public static void battle(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertPlayerNotDefeated(context.getSource().getPlayer());
            assertSelectedStartingPokemons(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new BattleFrontierPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new RandomTrainerBattleActorFactory().create(30)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.TRAINER_BATTLES.add(pokemonBattle);

                UUID playerUuid = context.getSource().getPlayer().getUuid();
                BattleFrontier.SESSIONS.get(playerUuid).battleUuid = pokemonBattle.getBattleId();
                return Unit.INSTANCE;
            });

            context.getSource().getPlayer().sendMessage(Text.literal("Battle Frontier battle started"));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You must create Battle Frontier session first"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session does not exists",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (BattleFrontierDefeatedPlayerException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You are defeated. Please create another Battle Frontier session"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Battle Frontier session expired due to defeat",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (StartingPokemonsNotSelectedException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Please select starting Pokemons"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Starting Pokemons are not selected",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    private static void assertPlayerNotDefeated(ServerPlayerEntity player) throws BattleFrontierDefeatedPlayerException {
        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        if (session.isDefeated) {
            throw new BattleFrontierDefeatedPlayerException();
        }
    }

    private static void assertSelectedStartingPokemons(ServerPlayerEntity player)
            throws StartingPokemonsNotSelectedException {
        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        if (session.partyPokemons.isEmpty()) {
            throw new StartingPokemonsNotSelectedException();
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

        BattleFrontierSession session = SESSIONS.get(player.getUuid());
        return Instant.now().isBefore(session.timestamp.plus(Duration.ofHours(24)));
    }

    public static void tradePokemon(CommandContext<ServerCommandSource> context) {
        try {
            assertExistValidSession(context.getSource().getPlayer());
            assertPlayerNotDefeated(context.getSource().getPlayer());
            assertExistDefeatedTrainer(context);

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
            Trainer lastDefeatedTrainer = session.defeatedTrainers.get(session.defeatedTrainers.size() - 1);
            Pokemon trainerPokemon = lastDefeatedTrainer.pokemons.get(trainerslot - 1);

            session.partyPokemons = new ArrayList<>(session.partyPokemons);
            session.partyPokemons.set(playerslot - 1, trainerPokemon.clone(true, true));

        } catch (ValidBattleFrontierSessionNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You do not have active Battle Frontier session"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Valid Battle Frontier session does not exists",
                    context.getSource().getPlayer().getGameProfile().getName()));

        } catch (BattleFrontierDefeatedPlayerException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You are defeated. Please create another Battle Frontier session"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Battle Frontier session expired due to defeat",
                    context.getSource().getPlayer().getGameProfile().getName()));

        }catch (DefeatedTrainerNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("You do not have any defeated trainers"));
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Defeated trainers do not exist",
                    context.getSource().getPlayer().getGameProfile().getName()));
        }
    }

    private static void assertExistDefeatedTrainer(CommandContext<ServerCommandSource> context)
            throws DefeatedTrainerNotExistException {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        if (session.defeatedTrainers.isEmpty()) {
            throw new DefeatedTrainerNotExistException();
        }
    }

    public static void showTradeablePokemons(CommandContext<ServerCommandSource> context) {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        Trainer lastDefeatedTrainer = session.defeatedTrainers.get(session.defeatedTrainers.size() - 1);
        printPokemons(context, lastDefeatedTrainer.pokemons);
    }

    public static void showPartyPokemons(CommandContext<ServerCommandSource> context) {
        printPokemons(context, SESSIONS.get(context.getSource().getPlayer().getUuid()).partyPokemons);
    }

    private static void printPokemons(CommandContext<ServerCommandSource> context, List<Pokemon> pokemons) {
        for (int i = 0; i < pokemons.size(); i++) {
            Pokemon pokemon = pokemons.get(i);

            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("[%d] ", i + 1)).formatted(Formatting.YELLOW).append(pokemon.getDisplayName()));
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

    public static void rerollPokemons(CommandContext<ServerCommandSource> context) {
        BattleFrontierSession session = SESSIONS.get(context.getSource().getPlayer().getUuid());
        if (session.isRerolled) return;

        session.partyPokemons = new RandomPartyPokemonsFactory().create();
        context.getSource().getPlayer().sendMessage(Text.literal("Rerolled Pokemons"));
    }
}
