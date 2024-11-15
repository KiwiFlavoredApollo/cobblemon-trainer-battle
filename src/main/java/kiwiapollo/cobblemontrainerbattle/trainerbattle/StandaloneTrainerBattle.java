package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;

import java.util.UUID;

public class StandaloneTrainerBattle implements TrainerBattle {
    private final BaseTrainerBattle battle;

    public StandaloneTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer
    ) {
        this.battle = new BaseTrainerBattle(player, trainer);
    }

    @Override
    public void start() throws BattleStartException {
        for (MessagePredicate<PlayerBattleParticipant> predicate : getTrainer().getPredicates()) {
            if (!predicate.test(getPlayer())) {
                getPlayer().sendErrorMessage(predicate.getMessage());
                throw new BattleStartException();
            }
        }

        battle.start();

        setBattleTheme();
    }

    private void setBattleTheme() {
        try {
            PokemonBattle battle =  Cobblemon.INSTANCE.getBattleRegistry().getBattle(getBattleId());
            PlayerBattleActor player = (PlayerBattleActor) battle.getActor(getPlayer().getPlayerEntity());
            player.setBattleTheme(CobblemonSounds.PVN_BATTLE);
        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public void onPlayerVictory() {
        getPlayer().onVictory();
        getTrainer().onDefeat();
    }

    @Override
    public void onPlayerDefeat() {
        getPlayer().onDefeat();
        getTrainer().onVictory();
    }

    @Override
    public UUID getBattleId() {
        return battle.getBattleId();
    }

    @Override
    public PlayerBattleParticipant getPlayer() {
        return battle.getPlayer();
    }

    @Override
    public TrainerBattleParticipant getTrainer() {
        return battle.getTrainer();
    }
}
