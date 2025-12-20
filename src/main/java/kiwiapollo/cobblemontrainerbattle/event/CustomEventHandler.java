package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.battle.BattleHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ShowdownTeamExporter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class CustomEventHandler {
    public static void initialize() {
        BattleVictoryEventHandler.initialize();
        LootDroppedEventHandler.initialize();
        ShowdownTeamExporter.initialize();

        ServerLifecycleEvents.SERVER_STARTED.register(new BattleHistoryStorage.Renamer());
        ServerLifecycleEvents.SERVER_STARTED.register(BattleHistoryStorage.getInstance());
        ServerLifecycleEvents.SERVER_STOPPED.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_SERVER_TICK.register(BattleHistoryStorage.getInstance());
        ServerTickEvents.END_WORLD_TICK.register(new BattleFledEventHandler());
        ServerTickEvents.END_WORLD_TICK.register(new TrainerEntitySpawner());
        ServerEntityEvents.ENTITY_LOAD.register(new DeprecatedEntityMigrator());
    }
}
