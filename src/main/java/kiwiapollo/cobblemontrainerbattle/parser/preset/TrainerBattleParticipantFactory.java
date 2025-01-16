package kiwiapollo.cobblemontrainerbattle.parser.preset;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.FlatLevelTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.NormalLevelTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.RelativeLevelTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.parser.pokemon.ShowdownPokemon;

import java.util.List;

public class TrainerBattleParticipantFactory implements SimpleFactory<TrainerBattleParticipant> {
    private final String id;
    private final TrainerPreset preset;
    private final List<ShowdownPokemon> team;

    public TrainerBattleParticipantFactory(String id, TrainerPreset preset, List<ShowdownPokemon> team) {
        this.id = id;
        this.preset = preset;
        this.team = team;
    }

    @Override
    public TrainerBattleParticipant create() {
        return switch(new LevelMode.Factory(preset.levelMode).create()) {
            case NORMAL -> new NormalLevelTrainer(id, preset, team);
            case RELATIVE -> new RelativeLevelTrainer(id, preset, team);
            case FLAT -> new FlatLevelTrainer(id, preset, team);
        };
    }
}
