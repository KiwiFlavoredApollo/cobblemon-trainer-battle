package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.ShowdownJsonExporter;
import kiwiapollo.cobblemontrainerbattle.economies.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.PlayerValidator;
import net.minecraft.server.command.ServerCommandSource;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.NAMESPACE);

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload"), String.format("%s.%s", getLiteral(), "export")))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload")))
                        .executes(context -> {
                            CobblemonTrainerBattle.config = ConfigLoader.load();
                            CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
                            CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "export")))
                        .executes(context -> {
                            try {
                                new PlayerValidator(context.getSource().getPlayer()).assertNotEmptyPlayerParty();
                                new ShowdownJsonExporter(context.getSource().getPlayer()).export();
                                return Command.SINGLE_SUCCESS;

                            } catch (EmptyPlayerPartyException e) {
                                CobblemonTrainerBattle.LOGGER.error("Player has no Pokemon");
                                return 0;
                            }
                        }));
    }
}
