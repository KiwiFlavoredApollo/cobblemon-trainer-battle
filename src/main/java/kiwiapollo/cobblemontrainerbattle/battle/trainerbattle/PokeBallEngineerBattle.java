package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.TrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBox;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.List;
import java.util.UUID;

public class PokeBallEngineerBattle extends CustomPokemonBattle {
    public PokeBallEngineerBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleActorFactory(player, trainer).create(), new TrainerBattleActorFactory(player, trainer).create());
    }

    private static class PlayerBattleActorFactory implements SimpleFactory<PlayerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
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
            return Cobblemon.INSTANCE.getStorage().getParty(player).toBattleTeam(false, false, null);
        }
    }

    private static class TrainerBattleActorFactory implements SimpleFactory<TrainerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public TrainerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

        @Override
        public TrainerBattleActor create() {
            return new TrainerBattleActor(
                    getTrainerTemplate(),
                    getUuid(),
                    getBattleTeam(),
                    getEntity(),
                    getOnPlayerVictoryHandler(),
                    getOnPlayerDefeatHandler()
            );
        }

        private TrainerTemplate getTrainerTemplate() {
            return trainer;
        }

        private UUID getUuid() {
            return trainer.getEntityUuid();
        }

        private List<BattlePokemon> getBattleTeam() {
            return toPartyStore(trainer.getTeam()).toBattleTeam(true, true, null);
        }

        private PartyStore toPartyStore(List<PokemonLevelPair> list) {
            PartyStore store = new PartyStore(trainer.getEntityUuid());

            for (PokemonLevelPair pair : list) {
                store.add(toPokemon(pair));
            }

            return store;
        }

        private Pokemon toPokemon(PokemonLevelPair pair) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            pokemon.setLevel(pair.getLevel());
            pokemon.heal();
            return pokemon;
        }

        private LivingEntity getEntity() {
            return (LivingEntity) player.getServerWorld().getEntity(trainer.getEntityUuid());
        }

        private Runnable getOnPlayerVictoryHandler() {
            return () -> {
                emitRedstonePulse();
            };
        }

        private Runnable getOnPlayerDefeatHandler() {
            return () -> {

            };
        }

        private void emitRedstonePulse() {
            try {
                ServerWorld world = player.getServerWorld();
                VillagerEntity villager = (VillagerEntity) world.getEntity(trainer.getEntityUuid());
                BlockPos pos = villager.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE).map(GlobalPos::getPos).get();
                BlockState state = world.getBlockState(pos);

                world.setBlockState(pos, state.with(PokeBallBox.POWERED, true), 3);
                world.updateNeighbor(pos, state.getBlock(), pos);
                world.scheduleBlockTick(pos, state.getBlock(), 20);

            } catch (NullPointerException | ClassCastException | IllegalArgumentException ignored) {

            }
        }
    }
}
