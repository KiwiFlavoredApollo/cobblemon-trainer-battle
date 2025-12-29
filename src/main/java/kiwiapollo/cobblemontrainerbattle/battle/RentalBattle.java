package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.entity.PokemonTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class RentalBattle extends AbstractPokemonBattle implements PokemonBattle {
    public static final int LEVEL = 50;
    public static final int POKEMON_COUNT = 3;

    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public RentalBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleSideFactory(player, trainer).create(), new TrainerBattleSideFactory(player, trainer).create());

        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        if (!isPlayerPokemonCount(RentalBattle.POKEMON_COUNT)) {
            player.sendMessage(getNoPlayerRentalPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerPokemonCount(RentalBattle.POKEMON_COUNT)) {
            player.sendMessage(getNoTrainerRentalPokemonErrorMessage());
            throw new BattleStartException();
        }

        super.start();
    }

    private Text getNoPlayerRentalPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalbattle.error.no_player_pokemon").formatted(Formatting.RED);
    }

    private Text getNoTrainerRentalPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalbattle.error.no_trainer_pokemon").formatted(Formatting.RED);
    }

    private static class PlayerBattleSideFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleSideFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

        public PlayerBattleActor create() {
            return new PlayerBattleActor(
                    getUuid(),
                    getBattleTeam()
            );
        }

        private UUID getUuid() {
            return player.getUuid();
        }

        private List<? extends BattlePokemon> getBattleTeam() {
            List<Pokemon> pokemon = RentalPokemonStorage.getInstance().get(player).stream().toList();
            List<Pokemon> rental = pokemon.stream().map(this::applyPokemonProperty).toList();
            return toPartyStore(rental).toBattleTeam(true, false, null);
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore party = new PartyStore(player.getUuid());

            for (Pokemon p : pokemon) {
                party.add(p);
            }

            return party;
        }

        private Pokemon applyPokemonProperty(Pokemon pokemon) {
            Pokemon clone = pokemon.clone(true, true);

            clone.setLevel(RentalBattle.LEVEL);
            clone.heal();
            PokemonProperties.Companion.parse("uncatchable=yes").apply(clone);

            return clone;
        }
    }

    private static class TrainerBattleSideFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private final UUID uuid;
        private final LivingEntity entity;
        private final List<Pokemon> pokemon;

        public TrainerBattleSideFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getUuidOrElse(trainer, UUID.randomUUID());
            this.entity = getEntityOrFallBackToPlayer(trainer, player);
            this.pokemon = getRentalPokemon(trainer);
        }

        private UUID getUuidOrElse(TrainerTemplate trainer, UUID uuid) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return uuid;
            }
        }

        private LivingEntity getEntityOrFallBackToPlayer(TrainerTemplate trainer, ServerPlayerEntity player) {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(trainer.getEntityUuid());

                if (entity.distanceTo(player) < TrainerBattleActor.FLEE_DISTANCE_IN_BLOCKS) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        private List<Pokemon> getRentalPokemon(TrainerTemplate trainer) {
            return toPokemon(trainer.getTeam());
        }

        public TrainerBattleActor create() {
            return new TrainerBattleActor(
                    getTrainerTemplate(),
                    getUuid(),
                    getBattleTeam(),
                    getEntity(),
                    getPlayerVictoryHandler(),
                    getPlayerDefeatHandler()
            );
        }

        private TrainerTemplate getTrainerTemplate() {
            return trainer;
        }

        private UUID getUuid() {
            return uuid;
        }

        private List<BattlePokemon> getBattleTeam() {
            return toPartyStore(pokemon).toBattleTeam(true, true, null);
        }

        private List<Pokemon> toPokemon(List<ShowdownPokemon> team) {
            List<Pokemon> list = new ArrayList<>();

            for (ShowdownPokemon showdown : team) {
                try {
                    Pokemon pokemon = new ShowdownPokemonParser().toCobblemonPokemon(showdown);
                    pokemon.setLevel(RentalBattle.LEVEL);
                    pokemon.heal();
                    PokemonProperties.Companion.parse("uncatchable=yes").apply(pokemon);

                    list.add(pokemon);

                } catch (PokemonParseException ignored) {

                }
            }

            return list;
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore party = new PartyStore(getEntity().getUuid());

            for (Pokemon p : pokemon) {
                party.add(p);
            }

            return party;
        }

        private LivingEntity getEntity() {
            return entity;
        }

        private Runnable getPlayerVictoryHandler() {
            return () -> {
                updateTradablePokemon();
                runPlayerVictoryCommands();
                runEntityLevelPlayerVictoryHandler();
            };
        }

        private Runnable getPlayerDefeatHandler() {
            return () -> {
                runPlayerDefeatCommands();
                runEntityLevelPlayerDefeatHandler();
            };
        }

        private void runEntityLevelPlayerVictoryHandler() {
            try {
                ((PokemonTrainerEntity) entity).onPlayerVictory();

            } catch (ClassCastException ignored) {

            }
        }

        private void runPlayerVictoryCommands() {
            trainer.getOnVictoryCommands().forEach(command -> execute(command, player));
        }

        private void runPlayerDefeatCommands() {
            trainer.getOnDefeatCommands().forEach(command -> execute(command, player));
        }

        private void execute(String command, ServerPlayerEntity player) {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
        }

        private void runEntityLevelPlayerDefeatHandler() {
            try {
                ((PokemonTrainerEntity) entity).onPlayerDefeat();

            } catch (ClassCastException ignored) {

            }
        }

        private void updateTradablePokemon() {
            TradablePokemonStorage.getInstance().get(player).setFirst(pokemon.get(0));
            TradablePokemonStorage.getInstance().get(player).setSecond(pokemon.get(1));
            TradablePokemonStorage.getInstance().get(player).setThird(pokemon.get(2));
        }
    }
}
