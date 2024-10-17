package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.VirtualTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.resulthandler.GenericResultHandler;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.NormalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.NormalBattleTrainer;
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
                    CobblemonTrainerBattle.trainerRegistry.keySet().stream()
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
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.NAMESPACE, resourcePath);
        Trainer trainer = CobblemonTrainerBattle.trainerRegistry.get(identifier);

        return startBattleWithTrainer(player, trainer);
    }

    private int startBattleWithRandomTrainer(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        Identifier identifier = new RandomTrainerIdentifierFactory().create();
        Trainer trainer = CobblemonTrainerBattle.trainerRegistry.get(identifier);

        return startBattleWithTrainer(player, trainer);
    }

    private int startBattleWithTrainer(ServerPlayerEntity player, Trainer trainer) {
        try {
            PlayerBattleParticipant playerBattleParticipant = new NormalBattlePlayer(player);
            TrainerBattleParticipant trainerBattleParticipant = new NormalBattleTrainer(trainer, player);
            ResultHandler resultHandler = new GenericResultHandler(player, trainer.onVictory(), trainer.onDefeat());

            TrainerBattle trainerBattle = new VirtualTrainerBattle(playerBattleParticipant, trainerBattleParticipant, resultHandler);
            trainerBattle.start();

            CobblemonTrainerBattle.trainerBattleRegistry.put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
