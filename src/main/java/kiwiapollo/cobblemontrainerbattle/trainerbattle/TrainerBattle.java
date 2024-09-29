package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.NameTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.common.NameTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.exceptions.FaintPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerNameNotExistException;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattle {
    public static void battleWithStatusQuo(CommandContext<ServerCommandSource> context, String trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new NameTrainerBattleActorFactory().create(new NameTrainerFactory().create(trainer))),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.TRAINER_BATTLES.add(pokemonBattle);
                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("You have no Pokemon"));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s : Player has no Pokemon",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (FaintPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("Your Pokemons are all fainted"));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s : Pokemons are all fainted",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (TrainerNameNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("Unknown trainer %s", trainer)));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Unknown trainer", trainer));
        }
    }

    public static void battleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, String trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer()).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(context.getSource().getPlayer(), 100)),
                    new BattleSide(new NameTrainerBattleActorFactory().create(new NameTrainerFactory().create(trainer))),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.TRAINER_BATTLES.add(pokemonBattle);
                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            context.getSource().getPlayer().sendMessage(Text.literal("You have no Pokemon"));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s : Player has no Pokemon",
                            context.getSource().getPlayer().getGameProfile().getName()));

        } catch (TrainerNameNotExistException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("Unknown trainer %s", trainer)));
            CobblemonTrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            CobblemonTrainerBattle.LOGGER.error(String.format("%s : Unknown trainer", trainer));
        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().isEmpty()) {
            throw new EmptyPlayerPartyException();
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player) throws FaintPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new FaintPlayerPartyException();
        }
    }
}
