package kiwiapollo.cobblemontrainerbattle.battle;

import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TrainerBattle implements PokemonBattleBehavior {
    private final CustomPokemonBattle battle;

    public TrainerBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        this.battle = createTrainerBattle(player, trainer);
    }

    private CustomPokemonBattle createTrainerBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        return switch (trainer.getLevelMode()) {
            case NORMAL -> new NormalLevelBattle(player, trainer);
            case RELATIVE -> new RelativeLevelBattle(player, trainer);
            case FLAT -> new FlatLevelBattle(player, trainer);
        };
    }

    @Override
    public void start() throws BattleStartException {
        this.battle.start();
    }

    @Override
    public UUID getBattleId() {
        return this.battle.getBattleId();
    }
}
