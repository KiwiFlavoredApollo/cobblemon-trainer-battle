package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattleStorage;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.ProfileExistPredicate;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.standalone.StandaloneNormalTrainerBattle;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class TrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleCommand() {
        super("trainerbattle");

        List<String> permissions = List.of(
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer"),
                String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random")
        );

        this.requires(new PlayerCommandSourcePredicate(permissions.toArray(String[]::new)))
                .then(getSelectedTrainerBattleCommand())
                .then(getRandomTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getSelectedTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "trainer");
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                .requires(new PlayerCommandSourcePredicate(permission))
                .suggests((context, builder) -> {
                    TrainerProfileStorage.getProfileRegistry().keySet().stream()
                            .map(Identifier::toString)
                            .forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(this::startBattleWithSelectedTrainer);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                .requires(new PlayerCommandSourcePredicate(permission))
                .executes(this::startBattleWithRandomTrainer);
    }

    private int startBattleWithSelectedTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();
            Identifier trainer = new Identifier(StringArgumentType.getString(context, "trainer"));

            MessagePredicate<Identifier> isTrainerProfileExist = new ProfileExistPredicate(TrainerProfileStorage.getProfileRegistry());
            if (!isTrainerProfileExist.test(trainer)) {
                player.sendMessage(isTrainerProfileExist.getErrorMessage());
                return 0;
            }

            TrainerBattle trainerBattle = new StandaloneNormalTrainerBattle(player, trainer);
            trainerBattle.start();

            TrainerBattleStorage.getTrainerBattleRegistry().put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }

    private int startBattleWithRandomTrainer(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();
            Identifier trainer = new RandomTrainerFactory().create();

            MessagePredicate<Identifier> isTrainerProfileExist = new ProfileExistPredicate(TrainerProfileStorage.getProfileRegistry());
            if (!isTrainerProfileExist.test(trainer)) {
                player.sendMessage(isTrainerProfileExist.getErrorMessage());
                return 0;
            }

            TrainerBattle trainerBattle = new StandaloneNormalTrainerBattle(player, trainer);
            trainerBattle.start();

            TrainerBattleStorage.getTrainerBattleRegistry().put(player.getUuid(), trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
