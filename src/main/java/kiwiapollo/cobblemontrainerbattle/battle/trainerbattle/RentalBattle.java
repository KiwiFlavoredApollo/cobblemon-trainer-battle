package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.TrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemonStorage;
import kiwiapollo.cobblemontrainerbattle.global.context.TradePokemonStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.UUID;

public class RentalBattle extends CustomPokemonBattle implements PokemonBattleBehavior {
    private static final int LEVEL = 50;

    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public RentalBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleSideFactory(player, trainer).create(), new TrainerBattleSideFactory(player, trainer).create());

        this.player = player;
        this.trainer = trainer;
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
            Pokemon clone = pokemon.clone(true, true);
            clone.heal();
            return clone;
        }
    }

    private static class TrainerBattleSideFactory implements SimpleFactory<TrainerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private final UUID uuid;

        public TrainerBattleSideFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getOrCreateUuid(trainer);
        }

        private UUID getOrCreateUuid(TrainerTemplate trainer) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return UUID.randomUUID();
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

        private Runnable getPlayerVictoryHandler() {
            return () -> {
                setTradePokemon(toPokemon(trainer.getTeam()));
            };
        }

        private Runnable getPlayerDefeatHandler() {
            return () -> {
                
            };
        }

        private List<BattlePokemon> getBattleTeam() {
            return toPartyStore(trainer.getTeam()).toBattleTeam(true, true, null);
        }

        private PartyStore toPartyStore(List<PokemonLevelPair> list) {
            PartyStore store = new PartyStore(getEntity().getUuid());

            for (PokemonLevelPair pair : list) {
                store.add(toPokemon(pair));
            }

            return store;
        }

        private Pokemon toPokemon(PokemonLevelPair pair) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            pokemon.setLevel(RentalBattle.LEVEL);
            pokemon.heal();
            return pokemon;
        }

        private LivingEntity getEntity() {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(uuid);

                if (entity.distanceTo(player) < getMaximumEntityDistance(world)) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        private int getMaximumEntityDistance(ServerWorld world) {
            return world.getGameRules().get(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS).get();
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
    }
}
