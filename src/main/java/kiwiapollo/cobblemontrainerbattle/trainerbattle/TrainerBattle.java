package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
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
import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionKey;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kotlin.Unit;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattle {
    public static final int FLAT_LEVEL = 100;

    public static int startTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithStatusQuo(context, trainer);

        } catch (CreateTrainerFailedException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }


    public static int startRandomBattleWithStatusQuo(CommandContext<ServerCommandSource> context) {
         Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
         return startSpecificTrainerBattleWithStatusQuo(context, trainer);
    }

    public static int startSpecificTrainerBattleWithStatusQuo(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertPlayerPartyAtOrAboveRelativeLevelThreshold(context.getSource().getPlayer());
            assertNotFaintPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertSatisfiedTrainerCondition(context.getSource().getPlayer(), trainer);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new StatusQuoPlayerBattleActorFactory().create(context.getSource().getPlayer())),
                    new BattleSide(new TrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Status Quo Pokemon Battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (TrainerConditionException e) {
            if (e.getConditionKey().equals(TrainerConditionKey.MINIMUM_PARTY_LEVEL)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Your Pokemon must be at or above level %d",
                                (Integer) e.getConditionValue())).formatted(Formatting.RED));
            }

            if (e.getConditionKey().equals(TrainerConditionKey.MAXIMUM_PARTY_LEVEL)) {
                context.getSource().getPlayer().sendMessage(
                        Text.literal(String.format("Your Pokemon must be at or below level %d",
                                (Integer) e.getConditionValue())).formatted(Formatting.RED));
            }

            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;

        } catch (InvalidPlayerStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int startTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        try {
            String trainerResourcePath = StringArgumentType.getString(context, "trainer");
            Trainer trainer = new SpecificTrainerFactory().create(context.getSource().getPlayer(), trainerResourcePath);
            return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);

        } catch (CreateTrainerFailedException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    public static int startRandomBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context) {
        Trainer trainer = new RandomTrainerFactory().create(context.getSource().getPlayer());
        return startSpecificTrainerBattleWithFlatLevelAndFullHealth(context, trainer);
    }

    public static int startSpecificTrainerBattleWithFlatLevelAndFullHealth(CommandContext<ServerCommandSource> context, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(context.getSource().getPlayer());
            assertNotExistPlayerParticipatingPokemonBattle(context.getSource().getPlayer());
            assertSatisfiedTrainerCondition(context.getSource().getPlayer(), trainer);

            Cobblemon.INSTANCE.getStorage().getParty(context.getSource().getPlayer()).forEach(Pokemon::recall);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new FlatLevelFullHealthPlayerBattleActorFactory()
                            .create(context.getSource().getPlayer(), FLAT_LEVEL)),
                    new BattleSide(new FlatLevelFullHealthTrainerBattleActorFactory()
                            .create(trainer, FLAT_LEVEL)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                CobblemonTrainerBattle.trainerBattles.put(context.getSource().getPlayer().getUuid(), pokemonBattle);

                context.getSource().getPlayer().sendMessage(
                        Text.literal("Flat Level Full Health Pokemon Battle started"));
                CobblemonTrainerBattle.LOGGER.info(String.format("%s: versus %s",
                        context.getSource().getPlayer().getGameProfile().getName(), trainer.name));

                return Unit.INSTANCE;
            });

            return Command.SINGLE_SUCCESS;

        } catch (TrainerConditionException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;

        } catch (InvalidPlayerStateException e) {
            context.getSource().getPlayer().sendMessage(
                    Text.literal("").formatted(Formatting.RED));
            CobblemonTrainerBattle.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    private static void assertSatisfiedTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionException {
        assertSatisfiedMinimumLevelTrainerCondition(player, trainer);
        assertSatisfiedMaximumLevelTrainerCondition(player, trainer);
    }

    private static void assertSatisfiedMaximumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int maximumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();
            boolean isAtOrBelowPartyMaximumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= maximumPartyLevel);

            if (!isAtOrBelowPartyMaximumLevel) {
                throw new TrainerConditionException(
                        String.format("Player did not satisfy maximum level condition: %s, %s", player, trainer.name),
                        TrainerConditionKey.MAXIMUM_PARTY_LEVEL,
                        maximumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelTrainerCondition(ServerPlayerEntity player, Trainer trainer)
            throws TrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int minimumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(new Identifier(trainer.name)).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();
            boolean isAtOrAbovePartyMinimumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= minimumPartyLevel);

            if (!isAtOrAbovePartyMinimumLevel) {
                throw new TrainerConditionException(
                        String.format("Player did not satisfy minimum level condition: %s, %s", player, trainer.name),
                        TrainerConditionKey.MINIMUM_PARTY_LEVEL,
                        minimumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new InvalidPlayerStateException(
                    String.format("Player has no Pokemon: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemons are under leveled: %s", player.getGameProfile().getName()));
        }
    }

    private static void assertNotFaintPlayerParty(ServerPlayerEntity player) throws InvalidPlayerStateException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new InvalidPlayerStateException(
                    String.format("Pokemons are all fainted: %s", player.getGameProfile().getName()));
        }
    }

    public static void assertNotExistPlayerParticipatingPokemonBattle(ServerPlayerEntity player)
            throws InvalidPlayerStateException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new InvalidPlayerStateException(
                    String.format("Already participating in another Pokemon battle: %s",
                            player.getGameProfile().getName()));
        }
    }
}
