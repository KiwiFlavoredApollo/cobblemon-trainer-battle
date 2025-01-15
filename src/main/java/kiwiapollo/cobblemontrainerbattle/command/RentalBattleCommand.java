package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.RentalBattlePlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.PlayerBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class RentalBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalBattleCommand() {
        super("rentalbattle");

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
                    TrainerStorage.getInstance().keySet().forEach(builder::suggest);
                    return builder.buildFuture();
                })
                .executes(this::startBattleWithSelectedTrainer);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                .requires(new PlayerCommandSourcePredicate(permission))
                .executes(context -> Command.SINGLE_SUCCESS);
    }

    private int startBattleWithSelectedTrainer(CommandContext<ServerCommandSource> context) {
        try {
            String trainer = StringArgumentType.getString(context, "trainer");
            ServerPlayerEntity player = context.getSource().getPlayer();

            TrainerBattleParticipant trainerBattleParticipant = TrainerStorage.getInstance().get(trainer);
            PlayerBattleParticipant playerBattleParticipant = new RentalBattlePlayer(player);

            TrainerBattle trainerBattle = new PlayerBackedTrainerBattle(playerBattleParticipant, trainerBattleParticipant);
            trainerBattle.start();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setTrainerBattle(trainerBattle);

            return Command.SINGLE_SUCCESS;

        } catch (BattleStartException e) {
            return 0;
        }
    }
}
