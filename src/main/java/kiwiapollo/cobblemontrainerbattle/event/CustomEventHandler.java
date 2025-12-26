package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.battle.BattleHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.command.exportpokemon.ShowdownTeamExporter;

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
