package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.TrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.global.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.global.preset.PokemonLevelPair;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RelativeLevelBattle extends CustomPokemonBattle {
    private final ServerPlayerEntity player;
    private final TrainerTemplate trainer;

    public RelativeLevelBattle(ServerPlayerEntity player, TrainerTemplate trainer) {
        super(new PlayerBattleActorFactory(player, trainer).create(), new TrainerBattleActorFactory(player, trainer).create());

        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        if (isBusyWithPokemonBattle()) {
            player.sendMessage(getPlayerBusyErrorMessage());
            throw new BattleStartException();
        }

        if (!isRematchAllowed()) {
            player.sendMessage(getRematchErrorMessage());
            throw new BattleStartException();
        }

        if (!isCooldownElapsed()) {
            player.sendMessage(getCooldownErrorMessage());
            throw new BattleStartException();
        }

        if (!isEqualToOrLessThanMaximumPartyLevel()) {
            player.sendMessage(getMaximumPartyLevelErrorMessage());
            throw new BattleStartException();
        }

        if (!isEqualToOrGreaterThanMinimumPartyLevel()) {
            player.sendMessage(getMinimumPartyLevelErrorMessage());
            throw new BattleStartException();
        }

        if (!isEqualToOrLessThanMaximumPartySize()) {
            player.sendMessage(getMaximumPartySizeErrorMessage());
            throw new BattleStartException();
        }

        if (!isEqualToOrGreaterThanMinimumPartySize()) {
            player.sendMessage(getMinimumPartySizeErrorMessage());
            throw new BattleStartException();
        }

        if (!hasRequiredAbility()) {
            player.sendMessage(getRequiredAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (!hasRequiredHeldItem()) {
            player.sendMessage(getRequiredHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if(!hasRequiredLabel()) {
            player.sendMessage(getRequiredLabelErrorMessage());
            throw new BattleStartException();
        }

        if (!hasRequiredMove()) {
            player.sendMessage(getRequiredMoveErrorMessage());
            throw new BattleStartException();
        }

        if (!hasRequiredPokemon()) {
            player.sendMessage(getRequiredPokemonErrorMessage());
            throw new BattleStartException();
        }

        if (!hasForbiddenAbility()) {
            player.sendMessage(getForbiddenAbilityErrorMessage());
            throw new BattleStartException();
        }

        if (!hasForbiddenHeldItem()) {
            player.sendMessage(getForbiddenHeldItemErrorMessage());
            throw new BattleStartException();
        }

        if(!hasForbiddenLabel()) {
            player.sendMessage(getForbiddenLabelErrorMessage());
            throw new BattleStartException();
        }

        if (!hasForbiddenMove()) {
            player.sendMessage(getForbiddenMoveErrorMessage());
            throw new BattleStartException();
        }

        if (!hasForbiddenPokemon()) {
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
            return Cobblemon.INSTANCE.getStorage().getParty(player).toBattleTeam(false, false, null);
        }
    }

    private static class TrainerBattleActorFactory implements SimpleFactory<TrainerBattleActor> {
        private final ServerPlayerEntity player;
        private final TrainerTemplate trainer;
        private UUID uuid;

        public TrainerBattleActorFactory(ServerPlayerEntity player, TrainerTemplate trainer) {
            this.player = player;
            this.trainer = trainer;
            this.uuid = getOrCreateUuid(trainer);
        }

        private UUID getOrCreateUuid(TrainerTemplate trainer) {
            if (trainer.getEntityUuid() != null) {
                return trainer.getEntityUuid();

            } else {
                return UUID.randomUUID();
            }
        }

        @Override
        public TrainerBattleActor create() {
            return new TrainerBattleActor(
                    getTrainerTemplate(),
                    getUuid(),
                    getBattleTeam(),
                    getEntity(),
                    getOnPlayerVictoryHandler(),
                    getOnPlayerDefeatHandler()
            );
        }

        private TrainerTemplate getTrainerTemplate() {
            return trainer;
        }

        private UUID getUuid() {
            return uuid;
        }

        private List<BattlePokemon> getBattleTeam() {
            return toPartyStore(trainer.getTeam()).toBattleTeam(true, true, null);
        }

        private PartyStore toPartyStore(List<PokemonLevelPair> list) {
            PartyStore store = new PartyStore(uuid);

            for (PokemonLevelPair pair : list) {
                store.add(toPokemon(pair));
            }

            return store;
        }

        private Pokemon toPokemon(PokemonLevelPair pair) {
            Pokemon pokemon = pair.getPokemon().clone(true, true);
            pokemon.setLevel(getPivotLevel() + pair.getLevel());
            pokemon.heal();
            return pokemon;
        }

        private int getPivotLevel() {
            return Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream().filter(Objects::nonNull).map(Pokemon::getLevel).max(Integer::compare).orElseThrow();
        }

        private LivingEntity getEntity() {
            try {
                ServerWorld world = player.getServerWorld();
                LivingEntity entity = (LivingEntity) world.getEntity(uuid);

                if (entity.distanceTo(player) < getMaximumEntityDistance(world)) {
                    return entity;
                } else {
                    return player;
                }

            } catch (ClassCastException | NullPointerException e) {
                return player;
            }
        }

        private int getMaximumEntityDistance(ServerWorld world) {
            return world.getGameRules().get(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS).get();
        }

        private Runnable getOnPlayerVictoryHandler() {
            return () -> {
                trainer.getOnVictoryCommands().forEach(command -> execute(command, player));
                getBattleRecord(player).setVictoryCount(getBattleRecord(player).getVictoryCount() + 1);
                CustomCriteria.DEFEAT_TRAINER_CRITERION.trigger(player);
            };
        }

        private Runnable getOnPlayerDefeatHandler() {
            return () -> {
                trainer.getOnDefeatCommands().forEach(command -> execute(command, player));
                getBattleRecord(player).setDefeatCount(getBattleRecord(player).getDefeatCount() + 1);
            };
        }

        private BattleRecord getBattleRecord(ServerPlayerEntity player) {
            return PlayerHistoryStorage.getInstance().getOrCreate(player.getUuid()).getOrCreate(trainer.getIdentifier());
        }

        private void execute(String command, ServerPlayerEntity player) {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
        }
    }
}
