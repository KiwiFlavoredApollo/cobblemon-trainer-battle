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
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.exceptions.FaintPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.exceptions.PlayerParticipatingPokemonBattleExistException;
import kiwiapollo.cobblemontrainerbattle.exceptions.PlayerPartyBelowMinimumLevelException;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattle {
    public static final int LEVEL = 100;

    public static void battleWithStatusQuo(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveMinimumLevel(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.TRAINER_BATTLES.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

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

        } catch (PlayerPartyBelowMinimumLevelException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal(String.format("You must have at least one Pokemon at or above level %d", TrainerFileParser.MINIMUM_LEVEL))
                            .formatted(Formatting.RED));
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
        }
    }

    public static void battleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());

            Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer()).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory()
                            .create(context.getSource().getPlayer(), LEVEL)),
                    new BattleSide(new FlatLevelFullHealthNameTrainerBattleActorFactory()
                            .create(trainer, LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.TRAINER_BATTLES.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

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
        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new EmptyPlayerPartyException();
        }
    }

    private static void assertPlayerPartyAtOrAboveMinimumLevel(ServerPlayerEntity player) throws PlayerPartyBelowMinimumLevelException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.MINIMUM_LEVEL)) {
            throw new PlayerPartyBelowMinimumLevelException();
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
