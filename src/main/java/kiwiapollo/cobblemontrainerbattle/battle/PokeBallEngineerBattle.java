package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBox;
import kiwiapollo.cobblemontrainerbattle.entity.PokemonTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PokeBallEngineerBattle extends AbstractPokemonBattle {
    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public PokeBallEngineerBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleActorFactory(player, trainer).create(), new TrainerBattleActorFactory(player, trainer).create());

        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        if (isPlayerBusyWithPokemonBattle()) {
            player.sendMessage(getPlayerBusyErrorMessage());
            throw new BattleStartException();
        }

        if (!isPlayerPokemonExist()) {
            player.sendMessage(getNoPlayerPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerPokemonExist()) {
            player.sendMessage(getNoTrainerPokemonErrorMessage());
            throw new BattleStartException();
        }

        super.start();
    }

    private static class PlayerBattleActorFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
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
            return Cobblemon.INSTANCE.getStorage().getParty(player).toBattleTeam(false, false, getLeadingPokemonUuid());
        }

        private UUID getLeadingPokemonUuid() {
            try {
                Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                        .filter(Objects::nonNull)
                        .filter(p -> !p.isFainted()).toList()
                        .get(0);

                return pokemon.getUuid();

            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    private static class TrainerBattleActorFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public TrainerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

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
            List<Pokemon> pokemon = toPokemon(trainer.getTeam());
            return toPartyStore(pokemon).toBattleTeam(true, true, null);
        }

        private List<Pokemon> toPokemon(List<ShowdownPokemon> team) {
            List<Pokemon> list = new ArrayList<>();

            for (ShowdownPokemon showdown : team) {
                try {
                    Pokemon pokemon = new ShowdownPokemonParser().toCobblemonPokemon(showdown);
                    pokemon.setLevel(showdown.level);
                    pokemon.heal();
                    PokemonProperties.Companion.parse("uncatchable=yes").apply(pokemon);

                    list.add(pokemon);

                } catch (PokemonParseException ignored) {

                }
            }

            return list;
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore store = new PartyStore(trainer.getEntityUuid());

            for (Pokemon p : pokemon) {
                store.add(p);
            }

            return store;
        }

        private LivingEntity getEntity() {
            return (LivingEntity) player.getServerWorld().getEntity(trainer.getEntityUuid());
        }

        private Runnable getOnPlayerVictoryHandler() {
            return () -> {
                emitRedstonePulse();
                runEntityLevelPlayerVictoryHandler();
            };
        }

        private Runnable getOnPlayerDefeatHandler() {
            return () -> {
                runEntityLevelPlayerDefeatHandler();
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

        private void runEntityLevelPlayerVictoryHandler() {
            try {
                ((PokemonTrainerEntity) getEntity()).onPlayerVictory();

            } catch (ClassCastException ignored) {

            }
        }

        private void runEntityLevelPlayerDefeatHandler() {
            try {
                ((PokemonTrainerEntity) getEntity()).onPlayerDefeat();

            } catch (ClassCastException ignored) {

            }
        }
    }
}
