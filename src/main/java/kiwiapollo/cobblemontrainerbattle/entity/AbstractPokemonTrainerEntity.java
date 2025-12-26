package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.BattleHistory;
import kiwiapollo.cobblemontrainerbattle.battle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.battle.BattleHistoryStorage;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplateStorage;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;

public abstract class AbstractPokemonTrainerEntity extends PathAwareEntity implements PokemonTrainerEntity {
    private UUID battleId;

    public AbstractPokemonTrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);

        this.battleId = null;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            return ActionResult.PASS;
        }

        if (!(player instanceof ServerPlayerEntity)) {
            return ActionResult.FAIL;
        }

        if (!hand.equals(Hand.MAIN_HAND)) {
            return ActionResult.PASS;
        }

        if (isBusyWithPokemonBattle()) {
            return ActionResult.FAIL;
        }

        startTrainerBattle((ServerPlayerEntity) player, hand);

        return super.interactMob(player, hand);
    }

    private boolean isBusyWithPokemonBattle() {
        try {
            return Objects.nonNull(Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId));

        } catch (NullPointerException e) {
            return false;
        }
    }

    private void startTrainerBattle(ServerPlayerEntity player, Hand hand) {
        try {
            TrainerTemplate trainer = createTrainerTemplate(getTrainer(), this);
            TrainerBattle battle = new TrainerBattle(player, trainer);
            battle.start();
            this.battleId = battle.getBattleId();

            this.setAiDisabled(true);
            this.setVelocity(0, 0, 0);
            this.velocityDirty = true;

            Criteria.PLAYER_INTERACTED_WITH_ENTITY.trigger(player, player.getStackInHand(hand), this);

        } catch (BattleStartException | NullPointerException ignored) {

        }
    }

    private TrainerTemplate createTrainerTemplate(Identifier trainer, LivingEntity entity) {
        TrainerTemplate template = TrainerTemplateStorage.getInstance().get(trainer);
        return new TrainerTemplateFactory(template, entity).create();
    }

    @Override
    public void onDeath(DamageSource source) {
        if (source.getSource() instanceof ServerPlayerEntity player) {
            BattleHistory history = getBattleHistory(player, getTrainer());
            history.setKillCount(history.getKillCount() + 1);
            CustomCriteria.KILL_TRAINER_CRITERION.trigger(player);
        }

        stopBattle();
        super.onDeath(source);
    }

    private BattleHistory getBattleHistory(ServerPlayerEntity player, Identifier trainer) {
        return BattleHistoryStorage.getInstance().get(player.getUuid(), trainer);
    }

    private void stopBattle() {
        try {
            PokemonBattle battle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId);
            ServerPlayerEntity player = battle.getPlayers().get(0);
            battle.writeShowdownAction(String.format(">forcelose %s", battle.getActor(player).showdownId));
            battle.end();

        } catch (NullPointerException ignored) {

        }
    }

    @Override
    public UUID getBattleId() {
        return battleId;
    }

    private static class TrainerTemplateFactory {
        private final TrainerTemplate template;
        private final LivingEntity entity;

        TrainerTemplateFactory(TrainerTemplate template, LivingEntity entity) {
            this.template = template;
            this.entity = entity;
        }

        public TrainerTemplate create() {
            return new TrainerTemplate(
                    template.getTeam(),

                    template.getIdentifier(),
                    template.getDisplayName(),
                    template.getLevelMode(),
                    template.getBattleFormat(),
                    template.getBattleAI(),
                    template.getBattleTheme(),
                    template.getTexture(),
                    entity.getUuid(),

                    template.getOnVictoryCommands(),
                    template.getOnDefeatCommands(),

                    template.getCooldownInSeconds(),
                    template.isSpawnAllowed(),
                    template.isRematchAllowed(),

                    template.getMaximumPartySize(),
                    template.getMinimumPartySize(),
                    template.getMaximumPartyLevel(),
                    template.getMinimumPartyLevel(),

                    template.getRequiredType(),
                    template.getRequiredLabel(),
                    template.getRequiredPokemon(),
                    template.getRequiredHeldItem(),
                    template.getRequiredAbility(),
                    template.getRequiredMove(),

                    template.getForbiddenType(),
                    template.getForbiddenLabel(),
                    template.getForbiddenPokemon(),
                    template.getForbiddenHeldItem(),
                    template.getForbiddenAbility(),
                    template.getForbiddenMove(),

                    template.getAllowedType(),
                    template.getAllowedLabel(),
                    template.getAllowedPokemon(),
                    template.getAllowedHeldItem(),
                    template.getAllowedAbility(),
                    template.getAllowedMove()
            );
        }
    }
}
