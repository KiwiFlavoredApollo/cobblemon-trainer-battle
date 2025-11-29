package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TrainerTemplate {
    private final List<PokemonLevelPair> team;

    private final Identifier identifier;
    private final String displayName;
    private final LevelMode levelMode;
    private final BattleFormat battleFormat;
    private final String battleAI;
    private final SoundEvent battleTheme;
    private final Identifier texture;
    private final UUID entityUuid;

    private final List<String> onVictoryCommands;
    private final List<String> onDefeatCommands;

    private final long cooldownInSeconds;
    private final boolean isSpawnAllowed;
    private final boolean isRematchAllowed;

    private final int maximumPartySize;
    private final int minimumPartySize;

    private final int maximumPartyLevel;
    private final int minimumPartyLevel;

    private final List<PokemonType> requiredType;
    private final List<String> requiredLabel;
    private final List<ShowdownPokemon> requiredPokemon;
    private final List<String> requiredHeldItem;
    private final List<String> requiredAbility;
    private final List<String> requiredMove;

    private final List<PokemonType> forbiddenType;
    private final List<String> forbiddenLabel;
    private final List<ShowdownPokemon> forbiddenPokemon;
    private final List<String> forbiddenHeldItem;
    private final List<String> forbiddenAbility;
    private final List<String> forbiddenMove;

    private final List<PokemonType> allowedType;
    private final List<String> allowedLabel;
    private final List<ShowdownPokemon> allowedPokemon;
    private final List<String> allowedHeldItem;
    private final List<String> allowedAbility;
    private final List<String> allowedMove;

    public TrainerTemplate(
            List<PokemonLevelPair> team,

            Identifier identifier,
            String displayName,
            LevelMode levelMode,
            BattleFormat battleFormat,
            String battleAI,
            SoundEvent battleTheme,
            Identifier texture, UUID entityUuid,

            List<String> onVictoryCommands,
            List<String> onDefeatCommands,

            long cooldownInSeconds,
            boolean isSpawnAllowed,
            boolean isRematchAllowed,

            int maximumPartySize,
            int minimumPartySize,

            int maximumPartyLevel,
            int minimumPartyLevel,

            List<PokemonType> requiredType,
            List<String> requiredLabel,
            List<ShowdownPokemon> requiredPokemon,
            List<String> requiredHeldItem,
            List<String> requiredAbility,
            List<String> requiredMove,

            List<PokemonType> forbiddenType,
            List<String> forbiddenLabel,
            List<ShowdownPokemon> forbiddenPokemon,
            List<String> forbiddenHeldItem,
            List<String> forbiddenAbility,
            List<String> forbiddenMove,

            List<PokemonType> allowedType,
            List<String> allowedLabel,
            List<ShowdownPokemon> allowedPokemon,
            List<String> allowedHeldItem,
            List<String> allowedAbility,
            List<String> allowedMove
    ) {
        this.team = team;

        this.identifier = identifier;
        this.displayName = displayName;
        this.levelMode = levelMode;
        this.battleFormat = battleFormat;
        this.battleAI = battleAI;
        this.battleTheme = battleTheme;
        this.texture = texture;
        this.entityUuid = entityUuid;

        this.onVictoryCommands = onVictoryCommands;
        this.onDefeatCommands = onDefeatCommands;

        this.cooldownInSeconds = cooldownInSeconds;
        this.isSpawnAllowed = isSpawnAllowed;
        this.isRematchAllowed = isRematchAllowed;

        this.maximumPartySize = maximumPartySize;
        this.minimumPartySize = minimumPartySize;

        this.maximumPartyLevel = maximumPartyLevel;
        this.minimumPartyLevel = minimumPartyLevel;

        this.requiredType = requiredType;
        this.requiredLabel = requiredLabel;
        this.requiredPokemon = requiredPokemon;
        this.requiredHeldItem = requiredHeldItem;
        this.requiredAbility = requiredAbility;
        this.requiredMove = requiredMove;

        this.forbiddenType = forbiddenType;
        this.forbiddenLabel = forbiddenLabel;
        this.forbiddenPokemon = forbiddenPokemon;
        this.forbiddenHeldItem = forbiddenHeldItem;
        this.forbiddenAbility = forbiddenAbility;
        this.forbiddenMove = forbiddenMove;

        this.allowedType = allowedType;
        this.allowedLabel = allowedLabel;
        this.allowedPokemon = allowedPokemon;
        this.allowedHeldItem = allowedHeldItem;
        this.allowedAbility = allowedAbility;
        this.allowedMove = allowedMove;
    }

    public List<PokemonLevelPair> getTeam() {
        return team;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public LevelMode getLevelMode() {
        return levelMode;
    }

    public BattleFormat getBattleFormat() {
        return battleFormat;
    }

    public String getBattleAI() {
        return battleAI;
    }

    public SoundEvent getBattleTheme() {
        return battleTheme;
    }

    public Identifier getTexture() {
        return texture;
    }

    public UUID getEntityUuid() {
        return entityUuid;
    }

    public List<String> getOnVictoryCommands() {
        return onVictoryCommands;
    }

    public List<String> getOnDefeatCommands() {
        return onDefeatCommands;
    }

    public long getCooldownInSeconds() {
        return cooldownInSeconds;
    }

    public boolean isSpawnAllowed() {
        return isSpawnAllowed;
    }

    public boolean isRematchAllowed() {
        return isRematchAllowed;
    }

    public int getMaximumPartySize() {
        return maximumPartySize;
    }

    public int getMinimumPartySize() {
        return minimumPartySize;
    }

    public int getMaximumPartyLevel() {
        return maximumPartyLevel;
    }

    public int getMinimumPartyLevel() {
        return minimumPartyLevel;
    }

    public List<String> getRequiredLabel() {
        return requiredLabel;
    }

    public List<ShowdownPokemon> getRequiredPokemon() {
        return requiredPokemon;
    }

    public List<String> getRequiredHeldItem() {
        return requiredHeldItem;
    }

    public List<String> getRequiredAbility() {
        return requiredAbility;
    }

    public List<String> getRequiredMove() {
        return requiredMove;
    }

    public List<PokemonType> getRequiredType() {
        return requiredType;
    }
    
    public List<String> getForbiddenLabel() {
        return forbiddenLabel;
    }

    public List<ShowdownPokemon> getForbiddenPokemon() {
        return forbiddenPokemon;
    }

    public List<String> getForbiddenHeldItem() {
        return forbiddenHeldItem;
    }

    public List<String> getForbiddenAbility() {
        return forbiddenAbility;
    }

    public List<String> getForbiddenMove() {
        return forbiddenMove;
    }

    public List<PokemonType> getForbiddenType() {
        return forbiddenType;
    }

    public List<String> getAllowedLabel() {
        return allowedLabel;
    }

    public List<ShowdownPokemon> getAllowedPokemon() {
        return allowedPokemon;
    }

    public List<String> getAllowedHeldItem() {
        return allowedHeldItem;
    }

    public List<String> getAllowedAbility() {
        return allowedAbility;
    }

    public List<String> getAllowedMove() {
        return allowedMove;
    }

    public List<PokemonType> getAllowedType() {
        return allowedType;
    }
}