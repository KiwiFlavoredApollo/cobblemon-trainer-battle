package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.postbattle.RecordedBattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.postbattle.BattleResultHandler;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.NormalBattleTrainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleCommand() {
        super("trainerbattle");

        this.requires(new PlayerCommandPredicate(
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")
                ))
                .then(getSpecificTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSpecificTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandPredicate(permission))
                .suggests((context, builder) -> {
                    CobblemonTrainerBattle.trainerProfileRegistry.keySet().stream()
                            .map(Identifier::toString)
                            .forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(this::startBattleWithSelectedTrainer);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                .requires(new PlayerCommandPredicate(permission))
                .executes(this::startBattleWithRandomTrainer);
    }

    private int startBattleWithSelectedTrainer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        Identifier trainer = new Identifier(StringArgumentType.getString(context, "trainer"));

        return startBattleWithTrainer(player, trainer);
    }

    private int startBattleWithRandomTrainer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        Identifier trainer = new RandomTrainerIdentifierFactory().create();

        return startBattleWithTrainer(player, trainer);
    }

    private int startBattleWithTrainer(ServerPlayerEntity player, Identifier trainer) {
        try {
            PlayerBattleParticipant playerBattleParticipant = new NormalBattlePlayer(player);
            TrainerBattleParticipant trainerBattleParticipant = new NormalBattleTrainer(trainer, player);

            TrainerProfile trainerProfile = CobblemonTrainerBattle.trainerProfileRegistry.get(trainer);
            BattleResultHandler battleResultHandler = new RecordedBattleResultHandler(player, trainer, trainerProfile.onVictory(), trainerProfile.onDefeat());

            TrainerBattle trainerBattle = new StandardTrainerBattle(playerBattleParticipant, trainerBattleParticipant, battleResultHandler);
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
