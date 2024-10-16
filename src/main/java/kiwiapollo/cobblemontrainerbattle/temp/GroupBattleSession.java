package kiwiapollo.cobblemontrainerbattle.temp;

import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.NormalBattleTrainer;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.TrainerBattleParticipant;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class GroupBattleSession implements Session {
    private final PlayerBattleParticipant player;
    private final List<Identifier> trainersToDefeat;
    private final ResultHandler resultHandler;

    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;

    private UUID battleId;

    public GroupBattleSession(
            ServerPlayerEntity player,
            List<Identifier> trainersToDefeat,
            ResultHandler resultHandler
    ) {
        this.player = new NormalBattlePlayer(player);
        this.trainersToDefeat = trainersToDefeat;
        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
        this.resultHandler = resultHandler;
    }

    @Override
    public void startBattle() throws BattleStartException {
        TrainerBattleParticipant trainer = new NormalBattleTrainer(
                CobblemonTrainerBattle.trainers.get(trainersToDefeat.get(defeatedTrainersCount)),
                player.getPlayerEntity()
        );
        ResultHandler resultHandler = new DummyResultHandler();
        TrainerBattle trainerBattle = new VirtualTrainerBattle(player, trainer, resultHandler);
        trainerBattle.start();
    }

    @Override
    public void onBattleVictory() {
        defeatedTrainersCount += 1;
    }

    @Override
    public void onBattleDefeat() {
        isPlayerDefeated = true;
    }

    @Override
    public void onSessionStop() {
        if (isDefeatedAllTrainers()) {
            resultHandler.onVictory();
        } else {
            resultHandler.onDefeat();
        }
    }

    public boolean isDefeatedAllTrainers() {
        return trainersToDefeat.size() == defeatedTrainersCount;
    }
}
