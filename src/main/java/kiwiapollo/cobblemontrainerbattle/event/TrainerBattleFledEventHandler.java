package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
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
        List<ServerPlayerEntity> players = world.getPlayers().stream().filter(this::isFledFromTrainerBattle).toList();

        players.stream().filter(player -> isHostileTrainerEntityBattle(getPokemonBattle(player))).forEach(this::setRandomPartyPokemonFaint);
        players.stream().map(this::getPokemonBattle).map(this::getTrainerEntities).flatMap(List::stream).forEach(e -> e.setAiDisabled(false));
        players.stream().map(this::getPokemonBattle).forEach(PokemonBattle::end);

        players.forEach(player -> CobblemonTrainerBattle.LOGGER.info("Battle was fled: {}", player.getGameProfile().getName()));
    }

    private boolean isFledFromTrainerBattle(ServerPlayerEntity player) {
        try {
            PokemonBattle battle = getPokemonBattle(player);
            FleeableBattleActor trainer = getFleeableBattleActors(battle).get(0);

            Vec3d playerPos = player.getPos();
            Vec3d trainerPos = trainer.getWorldAndPosition().getSecond();

            return playerPos.distanceTo(trainerPos) > trainer.getFleeDistance();

        } catch (NullPointerException | IndexOutOfBoundsException | NoSuchElementException e) {
            return false;
        }
    }

    private PokemonBattle getPokemonBattle(ServerPlayerEntity player) {
        UUID battleId = BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle().getBattleId();
        return Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId);
    }

    private List<TrainerEntity> getTrainerEntities(PokemonBattle battle) {
        return getEntityBackedTrainerBattleActors(battle).stream()
                .map(EntityBackedTrainerBattleActor::getEntity)
                .filter(Objects::nonNull)
                .filter(entity -> entity instanceof TrainerEntity)
                .map(entity -> (TrainerEntity) entity)
                .toList();
    }

    private List<FleeableBattleActor> getFleeableBattleActors(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(actor -> actor instanceof FleeableBattleActor)
                .map(actor -> (FleeableBattleActor) actor)
                .toList();
    }

    private List<EntityBackedTrainerBattleActor> getEntityBackedTrainerBattleActors(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(actor -> actor instanceof EntityBackedTrainerBattleActor)
                .map(actor -> ((EntityBackedTrainerBattleActor) actor))
                .toList();
    }

    private boolean isHostileTrainerEntityBattle(PokemonBattle battle) {
        return !getTrainerEntities(battle).stream()
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
}
