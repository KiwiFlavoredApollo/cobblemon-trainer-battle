package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.NullTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContext;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LootDroppedEventHandler implements Function1<LootDroppedEvent, Unit> {
    /**
     * LOOT_DROPPED event fires before BATTLE_VICTORY event.
     * Cobblemon Discord, Hiroku said: It's only used if the player kills the pokemon by hand, not by battle.
     * However, Pokémon drop loots when defeated in battles, at least on 1.5.1
     */
    @Override
    public Unit invoke(LootDroppedEvent event) {
        if (!(event.getEntity() instanceof PokemonEntity pokemon)) {
            return null;
        }

        if (isTrainerBattle(pokemon.getServer(), pokemon.getBattleId())) {
            event.cancel();
            CobblemonTrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
        }

        return Unit.INSTANCE;
    }

    private boolean isTrainerBattle(MinecraftServer server, UUID battleId) {
        List<UUID> battleIds = new ArrayList<>();

        server.getPlayerManager().getPlayerList().forEach(player -> {
            try {
                BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
                battleIds.add(context.getTrainerBattle().getBattleId());
            } catch (NullPointerException ignored) {

            }
        });

        return battleIds.contains(battleId);
    }
}
