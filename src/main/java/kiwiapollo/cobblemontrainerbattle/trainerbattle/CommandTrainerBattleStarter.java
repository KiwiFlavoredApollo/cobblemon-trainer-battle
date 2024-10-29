package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.mojang.brigadier.Command;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.factory.BattleParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.ResourceValidator;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import kiwiapollo.cobblemontrainerbattle.postbattle.BatchedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.RecordedBattleResultHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;

public class CommandTrainerBattleStarter {
    public static int startBattle(ServerPlayerEntity player, Identifier trainer, BattleParticipantFactory battleParticipantFactory) {
        try {
            ResourceValidator.assertTrainerExist(trainer);

            PlayerBattleParticipant playerBattleParticipant = battleParticipantFactory.createPlayer(player);
            TrainerBattleParticipant trainerBattleParticipant = battleParticipantFactory.createTrainer(trainer, player);

            TrainerProfile profile = ProfileRegistries.trainer.get(trainer);
            BattleResultHandler battleResultHandler = new BatchedBattleResultHandler(
                    new RecordedBattleResultHandler(player, trainer),
                    new PostBattleActionSetHandler(player, profile.onVictory(), profile.onDefeat())
            );

            TrainerBattle trainerBattle = new StandardTrainerBattle(playerBattleParticipant, trainerBattleParticipant, battleResultHandler);
            trainerBattle.start();

            TrainerBattleRegistry.put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (FileNotFoundException e) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.common.resource.trainer_not_found"));
            return 0;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
