package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.postbattle.ParameterizedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.DefeatedAllTrainersException;
import kiwiapollo.cobblemontrainerbattle.exception.DefeatedToTrainerException;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.session.Session;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class GroupBattleSession implements Session {
    private final ServerPlayerEntity player;
    private final List<Identifier> trainersToDefeat;
    private final BattleResultHandler battleResultHandler;
    private final BattleParticipantFactory battleParticipantFactory;

    private TrainerBattle lastTrainerBattle;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;

    public GroupBattleSession(
            ServerPlayerEntity player,
            List<Identifier> trainersToDefeat,
            BattleResultHandler battleResultHandler,
            BattleParticipantFactory battleParticipantFactory
    ) {
        this.player = player;
        this.trainersToDefeat = trainersToDefeat;
        this.battleResultHandler = battleResultHandler;
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

            BattleResultHandler battleResultHandler = new ParameterizedBattleResultHandler(this::onBattleVictory, this::onBattleDefeat);

            TrainerBattle trainerBattle = new StandardTrainerBattle(
                    playerBattleParticipant,
                    trainerBattleParticipant,
                    battleResultHandler
            );
            trainerBattle.start();

            TrainerBattleStorage.put(player.getUuid(), trainerBattle);

            this.lastTrainerBattle = trainerBattle;

        } catch (DefeatedToTrainerException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_to_trainer").formatted(Formatting.RED));
            throw new BattleStartException();

        } catch (DefeatedAllTrainersException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.groupbattle.startbattle.defeated_all_trainers").formatted(Formatting.RED));
            throw new BattleStartException();
        }
    }

    public void onBattleVictory() {
        defeatedTrainersCount += 1;
    }

    public void onBattleDefeat() {
        isPlayerDefeated = true;
    }

    @Override
    public void onSessionStop() {
        if (isDefeatedAllTrainers()) {
            battleResultHandler.onVictory();
        } else {
            battleResultHandler.onDefeat();
        }
    }

    @Override
    public int getDefeatedTrainersCount() {
        return defeatedTrainersCount;
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
