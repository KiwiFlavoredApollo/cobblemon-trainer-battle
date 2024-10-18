package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.resulthandler.SessionBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.SafetyCheckedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.*;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.DefeatedAllTrainersException;
import kiwiapollo.cobblemontrainerbattle.exception.DefeatedToTrainerException;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class GroupBattleSession implements Session {
    private final ServerPlayerEntity player;
    private final List<Identifier> trainersToDefeat;
    private final ResultHandler resultHandler;
    private final BattleParticipantFactory battleParticipantFactory;

    private TrainerBattle lastTrainerBattle;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;

    public GroupBattleSession(
            ServerPlayerEntity player,
            List<Identifier> trainersToDefeat,
            ResultHandler resultHandler,
            BattleParticipantFactory battleParticipantFactory
    ) {
        this.player = player;
        this.trainersToDefeat = trainersToDefeat;
        this.resultHandler = resultHandler;
        this.battleParticipantFactory = battleParticipantFactory;

        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
    }

    @Override
    public void startBattle() throws BattleStartException {
        try {
            assertPlayerNotDefeated();
            assertNotDefeatedAllTrainers();

            PlayerBattleParticipant playerBattleParticipant = battleParticipantFactory.createPlayer(player);

            Identifier trainer = trainersToDefeat.get(defeatedTrainersCount);
            TrainerBattleParticipant trainerBattleParticipant = battleParticipantFactory.createTrainer(trainer, player);

            ResultHandler resultHandler = new SessionBattleResultHandler(this::onBattleVictory, this::onBattleDefeat);

            TrainerBattle trainerBattle = new SafetyCheckedTrainerBattle(
                    playerBattleParticipant,
                    trainerBattleParticipant,
                    resultHandler
            );
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);

            this.lastTrainerBattle = trainerBattle;

        } catch (DefeatedToTrainerException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_to_trainer").formatted(Formatting.RED));
            throw new BattleStartException();

        } catch (DefeatedAllTrainersException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers").formatted(Formatting.RED));
            throw new BattleStartException();
        }
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

    private void assertPlayerNotDefeated() throws DefeatedToTrainerException {
        if (isPlayerDefeated) {
            throw new DefeatedToTrainerException();
        }
    }

    private void assertNotDefeatedAllTrainers() throws DefeatedAllTrainersException {
        if (isDefeatedAllTrainers()) {
            throw new DefeatedAllTrainersException();
        }
    }

    public boolean isDefeatedAllTrainers() {
        return trainersToDefeat.size() == defeatedTrainersCount;
    }
}
