package kiwiapollo.cobblemontrainerbattle.entities;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.FlatLevelFullHealthPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.player.StatusQuoPlayerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedFlatLevelFullHealthTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedTrainerBattleActorFactory;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleFlatCommand;
import kiwiapollo.cobblemontrainerbattle.common.UnsatisfiedTrainerConditionExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityBackedTrainerBattle {
    public static final int FLAT_LEVEL = 100;
    public static Map<UUID, PokemonBattle> trainerBattles = new HashMap<>();

    public static int startSpecificTrainerBattleWithStatusQuo(ServerPlayerEntity player, Trainer trainer, TrainerEntity trainerEntity) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotFaintPlayerParty();
            playerValidator.assertNotPlayerBusyWithPokemonBattle();
            playerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold();
            playerValidator.assertSatisfiedTrainerCondition(trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(player)),
                    new BattleSide(new EntityBackedTrainerBattleActorFactory().create(trainer, trainerEntity)),
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

        } catch (BusyPlayerException e) {
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

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(ServerPlayerEntity player, Trainer trainer, TrainerEntity trainerEntity) {
        try {
            PlayerValidator playerValidator = new PlayerValidator(player);
            playerValidator.assertNotEmptyPlayerParty();
            playerValidator.assertNotPlayerBusyWithPokemonBattle();

            Cobblemon.INSTANCE.getStorage().getParty(player).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory().create(player, FLAT_LEVEL)),
                    new BattleSide(new EntityBackedFlatLevelFullHealthTrainerBattleActorFactory().create(trainer, FLAT_LEVEL, trainerEntity)),
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

        } catch (BusyPlayerException e) {
            MutableText message = Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle");
            player.sendMessage(message.formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(String.format("Player is busy with Pokemon battle: %s", player.getGameProfile().getName()));
            return 0;
        }
    }
}
