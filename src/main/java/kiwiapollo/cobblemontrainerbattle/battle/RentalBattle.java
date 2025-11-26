package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.template.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentalBattle extends CustomPokemonBattle implements PokemonBattleBehavior {
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
            player.sendMessage(getRentalPlayerPokemonNotReadyErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerPokemonCount(RentalBattle.POKEMON_COUNT)) {
            player.sendMessage(getRentalTrainerPokemonNotReadyErrorMessage());
            throw new BattleStartException();
        }

        super.start();
    }

    private Text getRentalPlayerPokemonNotReadyErrorMessage() {
        return Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.rental_pokemon_not_exist").formatted(Formatting.RED);
    }

    private Text getRentalTrainerPokemonNotReadyErrorMessage() {
        return Text.translatable("command.cobblemontrainerbattle.error.rentalbattle.trainer_minimum_party_size").formatted(Formatting.RED);
    }

    private static class PlayerBattleSideFactory implements SimpleFactory<PlayerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleSideFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

        @Override
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
            return toPartyStore(RentalPokemonStorage.getInstance().get(player).stream().toList()).toBattleTeam(true, false, null);
        }

        private PartyStore toPartyStore(List<Pokemon> team) {
            PartyStore party = new PartyStore(player.getUuid());

            for (Pokemon pokemon : team) {
                party.add(toRentalPokemon(pokemon));
            }

            return party;
        }

        private Pokemon toRentalPokemon(Pokemon pokemon) {
            Pokemon rental = pokemon.clone(true, true);
            rental.heal();
            PokemonProperties.Companion.parse("uncatchable=yes").apply(rental);
            return rental;
        }
    }

    private static class TrainerBattleSideFactory implements SimpleFactory<TrainerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private final UUID uuid;
        private final LivingEntity entity;

        public TrainerBattleSideFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getUuidOrCreateRandom(trainer);
            this.entity = getEntityOrFallBackToPlayer(trainer, player);
        }

        private UUID getUuidOrCreateRandom(TrainerTemplate trainer) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return UUID.randomUUID();
            }
        }

        private LivingEntity getEntityOrFallBackToPlayer(TrainerTemplate trainer, ServerPlayerEntity player) {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(trainer.getEntityUuid());

                if (entity.distanceTo(player) < getMaximumEntityDistance(world)) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        @Override
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
            return toPartyStore(trainer.getTeam()).toBattleTeam(true, true, null);
        }

        // TODO Side Effect?
        // 부족한 만큼 추가하는게 부가효과면 잘라내는 것도 부가효과이지 않은가
        private PartyStore toPartyStore(List<PokemonLevelPair> team) {
            PartyStore store = new PartyStore(getEntity().getUuid());

            List<PokemonLevelPair> pokemon = new ArrayList<>(team);

            if (pokemon.size() > RentalBattle.POKEMON_COUNT) {
                pokemon = pokemon.subList(0, RentalBattle.POKEMON_COUNT);
            }

            for (PokemonLevelPair pair : pokemon) {
                store.add(toPokemon(pair));
            }

            return store;
        }

        private Pokemon toPokemon(PokemonLevelPair pair) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            pokemon.setLevel(RentalBattle.LEVEL);
            pokemon.heal();
            PokemonProperties.Companion.parse("uncatchable=yes").apply(pokemon);
            return pokemon;
        }

        private LivingEntity getEntity() {
            return entity;
        }

        private int getMaximumEntityDistance(ServerWorld world) {
            return world.getGameRules().get(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS).get();
        }

        private Runnable getPlayerVictoryHandler() {
            return () -> {
                setTradePokemon(toPokemon(trainer.getTeam()));
                runEntityLevelPlayerVictoryHandler();
                trainer.getOnVictoryCommands().forEach(command -> execute(command, player));
            };
        }

        private Runnable getPlayerDefeatHandler() {
            return () -> {
                runEntityLevelPlayerDefeatHandler();
                trainer.getOnDefeatCommands().forEach(command -> execute(command, player));
            };
        }

        private void runEntityLevelPlayerVictoryHandler() {
            try {
                ((TrainerEntityBehavior) entity).onPlayerVictory();

            } catch (ClassCastException ignored) {

            }
        }

        private void runEntityLevelPlayerDefeatHandler() {
            try {
                ((TrainerEntityBehavior) entity).onPlayerDefeat();

            } catch (ClassCastException ignored) {

            }
        }

        private void setTradePokemon(List<Pokemon> pokemon) {
            TradePokemonStorage.getInstance().get(player).setFirst(pokemon.get(0));
            TradePokemonStorage.getInstance().get(player).setSecond(pokemon.get(1));
            TradePokemonStorage.getInstance().get(player).setThird(pokemon.get(2));
        }

        private List<Pokemon> toPokemon(List<PokemonLevelPair> pair) {
            List<Pokemon> clone = pair.stream().map(p -> p.getPokemon().clone(true, true)).toList();

            clone.forEach(p -> p.setLevel(RentalBattle.LEVEL));
            clone.forEach(Pokemon::heal);

            return clone;
        }

        private void execute(String command, ServerPlayerEntity player) {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
        }
    }
}
