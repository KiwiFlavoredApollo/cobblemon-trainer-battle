package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.executor.EntityBattleStarter;
import kiwiapollo.cobblemontrainerbattle.command.executor.TrainerBattleStarter;
import kiwiapollo.cobblemontrainerbattle.command.predicate.MultiCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.command.predicate.PlayerCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.UUID;

public class EntityBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public EntityBattleCommand() {
        super("entitybattle");

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
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("uuid", StringArgumentType.word())
                .requires(new PlayerCommandSourcePredicate(permission))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            TrainerStorage.getInstance().keySet().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(new EntityBattleStarter.BetweenThisPlayerAndSelectedTrainer()));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomTrainerBattleCommand() {
        String permission = String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), "random");
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("uuid", StringArgumentType.word())
                .requires(new MultiCommandSourcePredicate(permission))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .executes(new EntityBattleStarter.BetweenThisPlayerAndRandomTrainer()));
    }
}
