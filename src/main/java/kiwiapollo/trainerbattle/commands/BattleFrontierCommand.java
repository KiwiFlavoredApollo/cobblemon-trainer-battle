package kiwiapollo.trainerbattle.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.trainerbattle.utilities.FlatLevelFullHealthTrainerBattleBuilder;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

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
                    List<Pokemon> playerParty = List.of(
                            PokemonSpecies.INSTANCE.random().create(20),
                            PokemonSpecies.INSTANCE.random().create(20),
                            PokemonSpecies.INSTANCE.random().create(20)
                    );

                    ServerPlayerEntity player = context.getSource().getPlayer();
                    Pokemon pikachu = PokemonSpecies.INSTANCE.getByIdentifier(
                            Identifier.of("cobblemon", "pikachu")
                    ).create(20);

                    Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                            BattleFormat.Companion.getGEN_9_SINGLES(),
                            new BattleSide(new PlayerBattleActor(
                                    player.getUuid(),
                                    playerParty.stream()
                                            .map(pokemon -> new BattlePokemon(
                                                    pokemon,
                                                    pokemon,
                                                    pokemonEntity -> Unit.INSTANCE
                                            ))
                                            .toList()
                            )),
                            new BattleSide(new TrainerBattleActor(
                                    "MyTrainer",
                                    UUID.randomUUID(),
                                    List.of(
                                            new BattlePokemon(
                                                    pikachu,
                                                    pikachu,
                                                    pokemonEntity -> Unit.INSTANCE
                                            )
                                    ),
                                    new RandomBattleAI()
                            )),
                            false
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
