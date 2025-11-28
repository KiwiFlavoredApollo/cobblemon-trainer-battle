package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.template.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kotlin.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class TrainerBattleActor extends AIBattleActor implements EntityBackedBattleActor<LivingEntity>, FleeableBattleActor, BattleResultHandler {
    private final LivingEntity entity;
    private final ServerWorld world;
    private final Vec3d position;
    private final Runnable playerVictoryHandler;
    private final Runnable playerDefeatHandler;
    private final TrainerTemplate template;

    public TrainerBattleActor(
            TrainerTemplate template,
            UUID uuid,
            List<BattlePokemon> pokemon,
            LivingEntity entity,
            Runnable playerVictoryHandler,
            Runnable playerDefeatHandler
    ) {
        super(uuid, pokemon, new BattleAIFactory(template).create());
        this.template = template;
        this.entity = entity;
        this.world = (ServerWorld) entity.getWorld();
        this.position = entity.getPos();
        this.playerVictoryHandler = playerVictoryHandler;
        this.playerDefeatHandler = playerDefeatHandler;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @NotNull
    @Override
    public ActorType getType() {
        return ActorType.NPC;
    }

    @NotNull
    @Override
    public MutableText getName() {
        String name = template.getDisplayName();

        if (name == null) {
            name = template.getIdentifier().toString();
        }

        return Text.translatable(name);
    }

    @NotNull
    @Override
    public MutableText nameOwned(@NotNull String s) {
        return Text.literal(s).append(getName());
    }

    @Override
    public float getFleeDistance() {
        return world.getGameRules().getInt(CustomGameRule.TRAINER_FLEE_DISTANCE_IN_BLOCKS);
    }

    @Nullable
    @Override
    public Pair<ServerWorld, Vec3d> getWorldAndPosition() {
        return new Pair<>(world, position);
    }

    @Override
    public void onPlayerVictory() {
        playerVictoryHandler.run();
    }

    @Override
    public void onPlayerDefeat() {
        playerDefeatHandler.run();
    }

    public SoundEvent getBattleTheme() {
        return template.getBattleTheme();
    }

    public BattleFormat getBattleFormat() {
        return template.getBattleFormat();
    }

    public Identifier getIdentifier() {
        return template.getIdentifier();
    }

    public int getMaximumPartyLevel() {
        return template.getMaximumPartyLevel();
    }

    public int getMinimumPartyLevel() {
        return template.getMinimumPartyLevel();
    }

    public int getMaximumPartySize() {
        return template.getMaximumPartySize();
    }

    public int getMinimumPartySize() {
        return template.getMinimumPartySize();
    }

    public Set<String> getRequiredType() {
        return new HashSet<>(template.getRequiredType());
    }

    public List<ShowdownPokemon> getRequiredPokemon() {
        return template.getRequiredPokemon();
    }

    public Set<String> getRequiredLabel() {
        return new HashSet<>(template.getRequiredLabel());
    }

    public Set<String> getRequiredMove() {
        return new HashSet<>(template.getRequiredMove());
    }

    public Set<Item> getRequiredHeldItem() {
        return template.getRequiredHeldItem().stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .map(Registries.ITEM::get)
                .collect(Collectors.toSet());
    }

    public Set<String> getRequiredAbility() {
        return new HashSet<>(template.getRequiredAbility());
    }

    public Set<String> getForbiddenType() {
        return new HashSet<>(template.getForbiddenType());
    }

    public List<ShowdownPokemon> getForbiddenPokemon() {
        return template.getForbiddenPokemon();
    }

    public Set<String> getForbiddenLabel() {
        return new HashSet<>(template.getForbiddenLabel());
    }

    public Set<String> getForbiddenMove() {
        return new HashSet<>(template.getForbiddenMove());
    }

    public Set<Item> getForbiddenHeldItem() {
        return template.getForbiddenHeldItem().stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .map(Registries.ITEM::get)
                .collect(Collectors.toSet());
    }

    public Set<String> getForbiddenAbility() {
        return new HashSet<>(template.getForbiddenAbility());
    }

    public long getCooldownInSeconds() {
        return template.getCooldownInSeconds();
    }

    public boolean isRematchAllowed() {
        return template.isRematchAllowed();
    }

    public Set<String> getAllowedType() {
        return new HashSet<>(template.getAllowedType());
    }

    public List<ShowdownPokemon> getAllowedPokemon() {
        return template.getAllowedPokemon();
    }

    public Set<String> getAllowedLabel() {
        return new HashSet<>(template.getAllowedLabel());
    }

    public Set<String> getAllowedMove() {
        return new HashSet<>(template.getAllowedMove());
    }

    public Set<Item> getAllowedHeldItem() {
        return template.getAllowedHeldItem().stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .map(Registries.ITEM::get)
                .collect(Collectors.toSet());
    }

    public Set<String> getAllowedAbility() {
        return new HashSet<>(template.getAllowedAbility());
    }

    public Set<String> getPerPokemonRequiredType() {
        return new HashSet<>(template.getPerPokemonRequiredType());
    }

    public Set<String> getPerPokemonRequiredLabel() {
        return new HashSet<>(template.getPerPokemonRequiredLabel());
    }

    public Set<String> getPerPokemonRequiredMove() {
        return new HashSet<>(template.getPerPokemonRequiredMove());
    }

    public Set<Item> getPerPokemonRequiredHeldItem() {
        return template.getPerPokemonRequiredHeldItem().stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .map(Registries.ITEM::get)
                .collect(Collectors.toSet());
    }

    public Set<String> getPerPokemonRequiredAbility() {
        return new HashSet<>(template.getPerPokemonRequiredAbility());
    }
}
