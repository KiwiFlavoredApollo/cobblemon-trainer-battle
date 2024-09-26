package kiwiapollo.trainerbattle.commands;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

public class BattleFrontierCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFrontierCommand() {
        super("battlefrontier");

        this.requires(new PlayerCommandPredicate(getLiteral()))
                .then(getBattleFrontierStartCommand())
                .then(getBattleFrontierStopCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .executes(context -> {
                    List<Pokemon> pokemons = List.of(
                            PokemonSpecies.INSTANCE.random().create(20),
                            PokemonSpecies.INSTANCE.random().create(20),
                            PokemonSpecies.INSTANCE.random().create(20)
                    );

                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStopCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stop")
                .executes(context -> {
                    return 0;
                });
    }
}
