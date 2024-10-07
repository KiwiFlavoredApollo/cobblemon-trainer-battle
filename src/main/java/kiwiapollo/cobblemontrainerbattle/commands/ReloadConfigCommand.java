package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.EconomyFactory;
import net.minecraft.server.command.ServerCommandSource;

public class ReloadConfigCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public ReloadConfigCommand() {
        super(CobblemonTrainerBattle.NAMESPACE);

        this.then(LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                .requires(new PlayerCommandPredicate(
                        String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())
                ))
                .executes(context -> {
                    CobblemonTrainerBattle.config = ConfigLoader.load();
                    CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
                    CobblemonTrainerBattle.LOGGER.info("Reloaded config.json");
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
