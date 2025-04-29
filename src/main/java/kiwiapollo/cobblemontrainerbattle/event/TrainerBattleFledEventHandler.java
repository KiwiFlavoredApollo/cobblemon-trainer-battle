package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.HostileTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.StreamSupport;

public class TrainerBattleFledEventHandler implements ServerTickEvents.EndWorldTick {
    @Override
    public void onEndTick(ServerWorld world) {
        world.getPlayers().forEach(player -> {
            try {
                endFledTrainerBattle(player);
            } catch (NullPointerException ignored) {

            }
        });
    }

    private void endFledTrainerBattle(ServerPlayerEntity player) {
        TrainerBattle trainerBattle = BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle();
        PokemonBattle pokemonBattle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(trainerBattle.getBattleId());
        if (!isFledBattle(pokemonBattle, player)) {
            return;
        }

        if (isHostileTrainerEntityBattle(pokemonBattle)) {
            setRandomPartyPokemonFaint(player);
        }

        StreamSupport.stream(pokemonBattle.getActors().spliterator(), false)
                .filter(battleActor -> battleActor instanceof EntityBackedTrainerBattleActor)
                .map(battleActor -> ((EntityBackedTrainerBattleActor) battleActor))
                .map(EntityBackedTrainerBattleActor::getEntity)
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof TrainerEntity)
                .forEach(entity -> ((TrainerEntity) entity).setAiDisabled(false));

        pokemonBattle.end();

        CobblemonTrainerBattle.LOGGER.info("Battle was fled: {}", player.getGameProfile().getName());
    }

    private boolean isHostileTrainerEntityBattle(PokemonBattle battle) {
        return !StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(battleActor -> battleActor instanceof EntityBackedTrainerBattleActor)
                .map(battleActor -> ((EntityBackedTrainerBattleActor) battleActor))
                .map(EntityBackedTrainerBattleActor::getEntity)
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof HostileTrainerEntity)
                .toList().isEmpty();
    }

    private void setRandomPartyPokemonFaint(ServerPlayerEntity player) {
        try {
            PartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player.getUuid());
            List<Pokemon> random = new ArrayList<>(party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .filter(pokemon -> !pokemon.isFainted())
                    .toList());
            Collections.shuffle(random);
            Pokemon pokemon = random.get(0);
            pokemon.setCurrentHealth(0);
            player.sendMessage(Text.translatable("cobblemon.battle.fainted", pokemon.getDisplayName()).formatted(Formatting.RED));

        } catch (NullPointerException | NoPokemonStoreException ignored) {

        }
    }

    private boolean isFledBattle(PokemonBattle trainerBattle, ServerPlayerEntity player) {
        try {
            FleeableBattleActor trainerBattleActor =
                    StreamSupport.stream(trainerBattle.getActors().spliterator(), false)
                            .filter(battleActor -> battleActor instanceof FleeableBattleActor)
                            .map(battleActor -> (FleeableBattleActor) battleActor).findFirst().get();

            Vec3d playerPos = player.getPos();
            Vec3d trainerPos = trainerBattleActor.getWorldAndPosition().getSecond();

            return playerPos.distanceTo(trainerPos) > trainerBattleActor.getFleeDistance();

        } catch (NullPointerException | NoSuchElementException | ClassCastException e) {
            return false;
        }
    }
}
