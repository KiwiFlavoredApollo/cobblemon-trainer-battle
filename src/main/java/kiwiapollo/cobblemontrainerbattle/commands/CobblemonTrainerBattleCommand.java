package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.economies.EconomyFactory;
import net.minecraft.server.command.ServerCommandSource;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.NAMESPACE);

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s", getLiteral(), "reload")
        )).then(LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload")))
                .executes(context -> {
                    CobblemonTrainerBattle.config = ConfigLoader.load();
                    CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
                    CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
