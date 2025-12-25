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

        ShowdownTeamExporter.Renamer.initialize();

        BattleHistoryStorage.initialize();

        BattleHistoryStorage.Renamer.initialize();

        BattleFledEventHandler.initialize();

        TrainerEntitySpawner.initialize();

        DeprecatedEntityMigrator.initialize();
    }
}
