package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.FlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.TrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.*;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainerBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            ResourceValidator.assertExistTrainerResource(trainerResourcePath);

            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithStatusQuo(context.getSource().getPlayer(), trainer);

        } catch (InvalidResourceStateException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", e.getResourcePath());
            context.getSource().getPlayer().sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading trainer file: %s, %s", e.getMessage(), e.getResourcePath()));
            return 0;
        }
    }

    public static int startRandomBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
         Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
         return startSpecificTrainerBattleWithStatusQuo(context.getSource().getPlayer(), trainer);
    }

    public static int startSpecificTrainerBattleWithStatusQuo(ServerPlayerEntity player, Trainer trainer) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotFaintPlayerParty();
            playerValidator.assertPlayerNotBusyWithPokemonBattle();
            playerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold();
            playerValidator.assertSatisfiedTrainerCondition(trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(player)),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                trainerBattles.put(player.getUuid(), pokemonBattle);

                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleCommand().getLiteral(), player.getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (EmptyPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (FaintedPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;

        }catch (BelowRelativeLevelThresholdException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Pokemon levels are below relative level threshold: %s", player.getGameProfile().getName()));
            return 0;

        } catch (UnsatisfiedTrainerConditionException e) {
            player.sendMessage(new UnsatisfiedTrainerConditionExceptionMessageFactory().create(e).formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Trainer condition not satisfied: %s, %s", e.getTrainerConditionType(), e.getRequiredValue()));
            return 0;
        }
    }

    public static int startTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            ResourceValidator.assertExistTrainerResource(trainerResourcePath);

            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context.getSource().getPlayer(), trainer);

        } catch (InvalidResourceStateException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", e.getResourcePath());
            context.getSource().getPlayer().sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("An error occurred while reading trainer file: %s, %s", e.getMessage(), e.getResourcePath()));
            return 0;
        }
    }

    public static int startRandomBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
        return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context.getSource().getPlayer(), trainer);
    }

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(ServerPlayerEntity player, Trainer trainer) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertPlayerNotBusyWithPokemonBattle();

            Cobblemon.INSTANCE.getStorage().getParty(player).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(player, FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, FLAT_LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                trainerBattles.put(player.getUuid(), pokemonBattle);

                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattleflat.success", trainer.name));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: %s versus %s",
                        new TrainerBattleFlatCommand().getLiteral(), player.getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (EmptyPlayerPartyException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.empty_player_party");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
            return 0;

        } catch (BusyWithPokemonBattleException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }
}
