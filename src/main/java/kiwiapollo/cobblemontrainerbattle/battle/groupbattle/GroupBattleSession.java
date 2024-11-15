package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattleStorage;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.AnyTrainerNotDefeatedPredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.PlayerNotDefeatedPredicate;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session.SessionTrainerBattleFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class GroupBattleSession implements Session {
    private final ServerPlayerEntity player;
    private final Identifier group;
    private final SessionTrainerBattleFactory factory;

    private final List<Identifier> trainersToDefeat;
    private final VictoryActionSetHandler sessionVictoryHandler;
    private final DefeatActionSetHandler sessionDefeatHandler;

    private TrainerBattle lastTrainerBattle;
    private int defeatedTrainersCount;
    private boolean isPlayerDefeated;

    public GroupBattleSession(ServerPlayerEntity player, Identifier group, SessionTrainerBattleFactory factory) {
        this.player = player;
        this.group = group;
        this.factory = factory;

        TrainerGroupProfile profile = TrainerGroupProfileStorage.getProfileRegistry().get(group);
        this.trainersToDefeat = profile.trainers.stream().map(Identifier::tryParse).toList();
        this.sessionVictoryHandler = new VictoryActionSetHandler(player, profile.onVictory);
        this.sessionDefeatHandler = new DefeatActionSetHandler(player, profile.onDefeat);

        this.defeatedTrainersCount = 0;
        this.isPlayerDefeated = false;
    }

    @Override
    public void startBattle() throws BattleStartException {
        List<MessagePredicate<GroupBattleSession>> predicates = List.of(
                new PlayerNotDefeatedPredicate<>(),
                new AnyTrainerNotDefeatedPredicate<>()
        );

        for (MessagePredicate<GroupBattleSession> predicate: predicates) {
            if (!predicate.test(this)) {
                player.sendMessage(predicate.getErrorMessage().formatted(Formatting.RED));
                throw new BattleStartException();
            }
        }

        TrainerBattle trainerBattle = factory.create(player, getNextTrainer(), this);
        trainerBattle.start();

        TrainerBattleStorage.getTrainerBattleRegistry().put(player.getUuid(), trainerBattle);

        this.lastTrainerBattle = trainerBattle;
    }

    private Identifier getNextTrainer() {
        return trainersToDefeat.get(defeatedTrainersCount);
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
        if (isAllTrainerDefeated()) {
            sessionVictoryHandler.run();
            PlayerHistoryManager.get(player.getUuid()).addPlayerVictory(group);
        } else {
            sessionDefeatHandler.run();
            PlayerHistoryManager.get(player.getUuid()).addPlayerDefeat(group);
        }
    }

    @Override
    public int getDefeatedTrainersCount() {
        return defeatedTrainersCount;
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates() {
        return List.of();
    }

    @Override
    public boolean isPlayerDefeated() {
        return isPlayerDefeated;
    }

    @Override
    public boolean isAllTrainerDefeated() {
        return trainersToDefeat.size() == defeatedTrainersCount;
    }

    @Override
    public boolean isAnyTrainerDefeated() {
        return defeatedTrainersCount > 0;
    }
}
