package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.entity.PokemonTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RelativeLevelBattle extends AbstractPokemonBattle {
    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public RelativeLevelBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleActorFactory(player, trainer).create(), new TrainerBattleActorFactory(player, trainer).create());

        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        if (isPlayerBusyWithPokemonBattle()) {
            player.sendMessage(getPlayerBusyErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerRematchAllowed()) {
            player.sendMessage(getRematchNotAllowedErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerCooldownElapsed()) {
            player.sendMessage(getCooldownNotElapsedErrorMessage());
            throw new BattleStartException();
        }

        if (!isPlayerPokemonExist()) {
            player.sendMessage(getNoPlayerPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerPokemonExist()) {
            player.sendMessage(getNoTrainerPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!isAtMostMaximumPartyLevel()) {
            player.sendMessage(getMaximumPartyLevelErrorMessage());
            throw new BattleStartException();
        }

        if (!isAtLeastMinimumPartyLevel()) {
            player.sendMessage(getMinimumPartyLevelErrorMessage());
            throw new BattleStartException();
        }

        if (!isAtMostMaximumPartySize()) {
            player.sendMessage(getMaximumPartySizeErrorMessage());
            throw new BattleStartException();
        }

        if (!isAtLeastMinimumPartySize()) {
            player.sendMessage(getMinimumPartySizeErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredType()) {
            player.sendMessage(getRequiredTypeErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredAbility()) {
            player.sendMessage(getRequiredAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredHeldItem()) {
            player.sendMessage(getRequiredHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredLabel()) {
            player.sendMessage(getRequiredLabelErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredMove()) {
            player.sendMessage(getRequiredMoveErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredPokemon()) {
            player.sendMessage(getRequiredPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenType()) {
            player.sendMessage(getForbiddenTypeErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenAbility()) {
            player.sendMessage(getForbiddenAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenHeldItem()) {
            player.sendMessage(getForbiddenHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenLabel()) {
            player.sendMessage(getForbiddenLabelErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenMove()) {
            player.sendMessage(getForbiddenMoveErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenPokemon()) {
            player.sendMessage(getForbiddenPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedType()) {
            player.sendMessage(getNotAllowedTypeErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedAbility()) {
            player.sendMessage(getNotAllowedAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedHeldItem()) {
            player.sendMessage(getNotAllowedHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedLabel()) {
            player.sendMessage(getNotAllowedLabelErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedMove()) {
            player.sendMessage(getNotAllowedMoveErrorMessage());
            throw new BattleStartException();
        }

        if (!hasOnlyAllowedPokemon()) {
            player.sendMessage(getNotAllowedPokemonErrorMessage());
            throw new BattleStartException();
        }

        super.start();
    }

    private static class PlayerBattleActorFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

        public PlayerBattleActor create() {
            return new PlayerBattleActor(
                    getUuid(),
                    getBattleTeam()
            );
        }

        private UUID getUuid() {
            return player.getUuid();
        }

        private List<? extends BattlePokemon> getBattleTeam() {
            return Cobblemon.INSTANCE.getStorage().getParty(player).toBattleTeam(false, false, getLeadingPokemonUuid());
        }

        private UUID getLeadingPokemonUuid() {
            try {
                Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                        .filter(Objects::nonNull)
                        .filter(p -> !p.isFainted()).toList()
                        .get(0);

                return pokemon.getUuid();

            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
    }

    private static class TrainerBattleActorFactory {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private final UUID uuid;
        private final LivingEntity entity;

        public TrainerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getUuidOrElse(trainer, UUID.randomUUID());
            this.entity = getEntityOrFallBackToPlayer(trainer, player);
        }

        private UUID getUuidOrElse(TrainerTemplate trainer, UUID uuid) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return uuid;
            }
        }

        private LivingEntity getEntityOrFallBackToPlayer(TrainerTemplate trainer, ServerPlayerEntity player) {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(trainer.getEntityUuid());

                if (entity.distanceTo(player) < TrainerBattleActor.FLEE_DISTANCE_IN_BLOCKS) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        public TrainerBattleActor create() {
            return new TrainerBattleActor(
                    getTrainerTemplate(),
                    getUuid(),
                    getBattleTeam(),
                    getEntity(),
                    getPlayerVictoryHandler(),
                    getPlayerDefeatHandler()
            );
        }

        private TrainerTemplate getTrainerTemplate() {
            return trainer;
        }

        private UUID getUuid() {
            return uuid;
        }

        private List<BattlePokemon> getBattleTeam() {
            List<Pokemon> pokemon = toPokemon(trainer.getTeam());
            return toPartyStore(pokemon).toBattleTeam(true, true, null);
        }

        private List<Pokemon> toPokemon(List<ShowdownPokemon> team) {
            List<Pokemon> list = new ArrayList<>();

            for (ShowdownPokemon showdown : team) {
                try {
                    Pokemon pokemon = new ShowdownPokemonParser().toCobblemonPokemon(showdown);
                    pokemon.setLevel(getPivotLevel() + showdown.level);
                    pokemon.heal();
                    PokemonProperties.Companion.parse("uncatchable=yes").apply(pokemon);

                    list.add(pokemon);

                } catch (PokemonParseException ignored) {

                }
            }

            return list;
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore party = new PartyStore(uuid);

            for (Pokemon p : pokemon) {
                party.add(p);
            }

            return party;
        }

        private int getPivotLevel() {
            return Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .max(Integer::compare)
                    .orElse(0);
        }

        private LivingEntity getEntity() {
            return entity;
        }

        private Runnable getPlayerVictoryHandler() {
            return () -> {
                runPlayerVictoryCommands();
                incrementPlayerVictoryCount();
                triggerDefeatTrainerCriterion();
                runEntityLevelPlayerVictoryHandler();
            };
        }

        private Runnable getPlayerDefeatHandler() {
            return () -> {
                runPlayerDefeatCommands();
                incrementPlayerDefeatCount();
                runEntityLevelPlayerDefeatHandler();
            };
        }

        private void runPlayerVictoryCommands() {
            trainer.getOnVictoryCommands().forEach(command -> execute(command, player));
        }

        private void runPlayerDefeatCommands() {
            trainer.getOnDefeatCommands().forEach(command -> execute(command, player));
        }

        private void execute(String command, ServerPlayerEntity player) {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
        }

        private void incrementPlayerVictoryCount() {
            BattleHistory history = getBattleHistory(player, trainer);
            history.setVictoryCount(history.getVictoryCount() + 1);
        }

        private void incrementPlayerDefeatCount() {
            BattleHistory history = getBattleHistory(player, trainer);
            history.setDefeatCount(history.getDefeatCount() + 1);
        }

        private BattleHistory getBattleHistory(ServerPlayerEntity player, TrainerTemplate trainer) {
            return BattleHistoryStorage.getInstance().get(player.getUuid(), trainer.getIdentifier());
        }

        private void triggerDefeatTrainerCriterion() {
            CustomCriteria.DEFEAT_TRAINER_CRITERION.trigger(player);
        }

        private void runEntityLevelPlayerVictoryHandler() {
            try {
                ((PokemonTrainerEntity) entity).onPlayerVictory();

            } catch (ClassCastException ignored) {

            }
        }

        private void runEntityLevelPlayerDefeatHandler() {
            try {
                ((PokemonTrainerEntity) entity).onPlayerDefeat();

            } catch (ClassCastException ignored) {

            }
        }
    }
}
