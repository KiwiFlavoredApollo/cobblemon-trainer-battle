package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityBehavior;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.template.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlatLevelBattle extends CustomPokemonBattle {
    private static final int LEVEL = 50;

    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public FlatLevelBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
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

        if (!isPlayerPokemonReady()) {
            player.sendMessage(getPlayerPokemonNotReadyErrorMessage());
            throw new BattleStartException();
        }

        if (!isTrainerPokemonReady()) {
            player.sendMessage(getTrainerPokemonNotReadyErrorMessage());
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

        if (!hasAllRequiredAbility()) {
            player.sendMessage(getRequiredAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (!hasAllRequiredHeldItem()) {
            player.sendMessage(getRequiredHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if(!hasAllRequiredLabel()) {
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

        if (hasAnyForbiddenAbility()) {
            player.sendMessage(getForbiddenAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (hasAnyForbiddenHeldItem()) {
            player.sendMessage(getForbiddenHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if(hasAnyForbiddenLabel()) {
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

        super.start();
    }

    private static class PlayerBattleActorFactory implements SimpleFactory<PlayerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;

        public PlayerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
        }

        @Override
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
            List<Pokemon> pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream().filter(Objects::nonNull).toList();
            List<Pokemon> flat = pokemon.stream().map(this::applyPokemonProperty).toList();
            return toPartyStore(flat).toBattleTeam(true, false, null);
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore party = new PartyStore(player.getUuid());

            for (Pokemon p : pokemon) {
                party.add(p);
            }

            return party;
        }

        private Pokemon applyPokemonProperty(Pokemon pokemon) {
            Pokemon clone = pokemon.clone(true, true);

            clone.setLevel(FlatLevelBattle.LEVEL);
            clone.heal();
            PokemonProperties.Companion.parse("uncatchable=yes").apply(clone);

            return clone;
        }
    }

    private static class TrainerBattleActorFactory implements SimpleFactory<TrainerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private final UUID uuid;
        private final LivingEntity entity;

        public TrainerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getUuidOrCreateRandom(trainer);
            this.entity = getEntityOrFallBackToPlayer(trainer, player);
        }

        private UUID getUuidOrCreateRandom(TrainerTemplate trainer) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return UUID.randomUUID();
            }
        }

        private LivingEntity getEntityOrFallBackToPlayer(TrainerTemplate trainer, ServerPlayerEntity player) {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(trainer.getEntityUuid());

                if (entity.distanceTo(player) < getMaximumEntityDistance(world)) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        @Override
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
            List<Pokemon> pokemon = trainer.getTeam().stream().map(this::toPokemon).toList();
            List<Pokemon> flat = pokemon.stream().map(this::applyPokemonProperty).toList();
            return toPartyStore(flat).toBattleTeam(true, true, null);
        }

        private Pokemon applyPokemonProperty(Pokemon pokemon) {
            Pokemon clone = pokemon.clone(true, true);

            clone.setLevel(FlatLevelBattle.LEVEL);
            clone.heal();
            PokemonProperties.Companion.parse("uncatchable=yes").apply(clone);

            return clone;
        }

        private PartyStore toPartyStore(List<Pokemon> pokemon) {
            PartyStore store = new PartyStore(uuid);

            for (Pokemon p : pokemon) {
                store.add(p);
            }

            return store;
        }

        private Pokemon toPokemon(PokemonLevelPair pair) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            pokemon.setLevel(pair.getLevel());
            return pokemon;
        }

        private LivingEntity getEntity() {
            return entity;
        }

        private int getMaximumEntityDistance(ServerWorld world) {
            return world.getGameRules().get(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS).get();
        }

        private Runnable getPlayerVictoryHandler() {
            return () -> {
                trainer.getOnVictoryCommands().forEach(command -> execute(command, player));
                runEntityLevelPlayerVictoryHandler();
                getBattleRecord(player).setVictoryCount(getBattleRecord(player).getVictoryCount() + 1);
                CustomCriteria.DEFEAT_TRAINER_CRITERION.trigger(player);
            };
        }

        private Runnable getPlayerDefeatHandler() {
            return () -> {
                trainer.getOnDefeatCommands().forEach(command -> execute(command, player));
                runEntityLevelPlayerDefeatHandler();
                getBattleRecord(player).setDefeatCount(getBattleRecord(player).getDefeatCount() + 1);
            };
        }

        private void runEntityLevelPlayerVictoryHandler() {
            try {
                ((TrainerEntityBehavior) entity).onPlayerVictory();

            } catch (ClassCastException ignored) {

            }
        }

        private void runEntityLevelPlayerDefeatHandler() {
            try {
                ((TrainerEntityBehavior) entity).onPlayerDefeat();

            } catch (ClassCastException ignored) {

            }
        }

        private BattleRecord getBattleRecord(ServerPlayerEntity player) {
            return PlayerHistoryStorage.getInstance().get(player).get(trainer.getIdentifier());
        }

        private void execute(String command, ServerPlayerEntity player) {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
        }
    }
}
