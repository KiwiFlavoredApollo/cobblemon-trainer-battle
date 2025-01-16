package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.Optional;

public interface TrainerBattleParticipant extends BattleParticipant {
    String getId();

    BattleAI getBattleAI();

    BattleFormat getBattleFormat();

    Optional<SoundEvent> getBattleTheme();

    LevelMode getLevelMode();

    List<BattlePokemon> getBattleTeam(ServerPlayerEntity player);

    boolean isSpawningAllowed();

    List<MessagePredicate<PlayerBattleParticipant>> getPredicates();

    AIBattleActor createBattleActor(ServerPlayerEntity player);

    void onPlayerDefeat(ServerPlayerEntity player);

    void onPlayerVictory(ServerPlayerEntity player);
}
