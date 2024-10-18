package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.resulthandler.RecordedResultHandler;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.StandardTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.common.TrainerProfile;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.VirtualNormalBattleTrainer;
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
                            .map(Identifier::getPath)
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

        String resourcePath = StringArgumentType.getString(context, "trainer");
        Identifier trainer = Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath);

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
            TrainerBattleParticipant trainerBattleParticipant = new VirtualNormalBattleTrainer(trainer, player);

            TrainerProfile trainerProfile = CobblemonTrainerBattle.trainerProfileRegistry.get(trainer);
            ResultHandler resultHandler = new RecordedResultHandler(player, trainerProfile.onVictory(), trainerProfile.onDefeat());

            TrainerBattle trainerBattle = new StandardTrainerBattle(playerBattleParticipant, trainerBattleParticipant, resultHandler);
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
