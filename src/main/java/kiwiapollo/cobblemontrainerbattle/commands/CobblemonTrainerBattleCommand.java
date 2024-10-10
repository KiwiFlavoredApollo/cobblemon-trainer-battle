package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ConfigLoader;
import kiwiapollo.cobblemontrainerbattle.common.ShowdownJsonExporter;
import kiwiapollo.cobblemontrainerbattle.economies.EconomyFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.PlayerValidator;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CobblemonTrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public CobblemonTrainerBattleCommand() {
        super(CobblemonTrainerBattle.NAMESPACE);

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload"), String.format("%s.%s", getLiteral(), "export")))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("reload")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "reload")))
                        .executes(this::reloadConfig))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("export")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s", getLiteral(), "export")))
                        .then(RequiredArgumentBuilder.<ServerCommandSource, EntitySelector>
                                        argument("player", EntityArgumentType.player())
                                .executes(this::exportPlayerParty)));
    }

    private int reloadConfig(CommandContext<ServerCommandSource> context) {
        CobblemonTrainerBattle.config = ConfigLoader.load();
        CobblemonTrainerBattle.economy = EconomyFactory.create(CobblemonTrainerBattle.config.economy);
        CobblemonTrainerBattle.LOGGER.info("Reloaded configuration");
        return Command.SINGLE_SUCCESS;
    }

    private int exportPlayerParty(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            new PlayerValidator(player).assertNotEmptyPlayerParty();
            new ShowdownJsonExporter(player).export();
            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            CobblemonTrainerBattle.LOGGER.error("Unknown player");
            return 0;

        } catch (EmptyPlayerPartyException e) {
            CobblemonTrainerBattle.LOGGER.error("Player has no Pokemon");
            return 0;
        }
    }
}
