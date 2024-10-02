package kiwiapollo.cobblemontrainerbattle.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.ValidBattleStreakNotExistException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class BattleStreakCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    protected BattleStreakCommand() {
        super("battlestreak");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("battlestreak", StringArgumentType.string())
                        .executes(context -> {
                            String battleStreakName = StringArgumentType.getString(context, "battlestreak");
                            try {
                                assertExistValidBattleStreak(battleStreakName);

                                // start battle

                                return Command.SINGLE_SUCCESS;

                            } catch (ValidBattleStreakNotExistException e) {
                                return -1;
                            }
                        }))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .executes(context -> {
                            try {
                                return Command.SINGLE_SUCCESS;

                            } catch (Exception e) {
                                return -1;
                            }
                        }));
    }

    private void assertExistValidBattleStreak(String battleStreakName) throws ValidBattleStreakNotExistException {
        if (!CobblemonTrainerBattle.battleStreaks.containsKey(new Identifier(battleStreakName))) {
            throw new ValidBattleStreakNotExistException();
        }

        JsonObject battleStreak = CobblemonTrainerBattle.battleStreaks.get(new Identifier(battleStreakName));
        if (!battleStreak.has("trainers")
                || !battleStreak.get("trainers").isJsonArray()
                || !battleStreak.get("trainers").getAsJsonArray().isEmpty()
        ) {
            throw new ValidBattleStreakNotExistException();
        }
    }
}
