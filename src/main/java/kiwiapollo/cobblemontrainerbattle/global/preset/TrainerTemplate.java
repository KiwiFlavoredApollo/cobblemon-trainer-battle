package kiwiapollo.cobblemontrainerbattle.global.preset;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class TrainerTemplate {
    private final List<PokemonLevelPair> team;

    private final Identifier identifier;
    private final Text displayName;
    private final LevelMode levelMode;
    private final BattleFormat battleFormat;
    private final BattleAI battleAI;
    private final SoundEvent battleTheme;
    private final Identifier texture;
    private final UUID entityUuid;

    private final List<String> onVictoryCommands;
    private final List<String> onDefeatCommands;

    private final boolean isSpawningAllowed;

    private final CooldownElapsedPredicate cooldownElapsedPredicate;
    private final RematchAllowedPredicate rematchAllowedPredicate;

    private final MaximumPartySizePredicate.PlayerPredicate maximumPartySizePredicate;
    private final MinimumPartySizePredicate.PlayerPredicate minimumPartySizePredicate;

    private final MaximumPartyLevelPredicate maximumPartyLevelPredicate;
    private final MinimumPartyLevelPredicate minimumPartyLevelPredicate;

    private final RequiredLabelPredicate requiredLabelPredicate;
    private final RequiredPokemonPredicate requiredPokemonPredicate;
    private final RequiredHeldItemPredicate requiredHeldItemPredicate;
    private final RequiredAbilityPredicate requiredAbilityPredicate;
    private final RequiredMovePredicate requiredMovePredicate;

    private final ForbiddenLabelPredicate forbiddenLabelPredicate;
    private final ForbiddenPokemonPredicate forbiddenPokemonPredicate;
    private final ForbiddenHeldItemPredicate forbiddenHeldItemPredicate;
    private final ForbiddenAbilityPredicate forbiddenAbilityPredicate;
    private final ForbiddenMovePredicate forbiddenMovePredicate;

    public TrainerTemplate(
            List<PokemonLevelPair> team,

            Identifier identifier,
            Text displayName,
            LevelMode levelMode,
            BattleFormat battleFormat,
            BattleAI battleAI,
            SoundEvent battleTheme,
            Identifier texture, UUID entityUuid,

            List<String> onVictoryCommands,
            List<String> onDefeatCommands,

            boolean isSpawningAllowed,

            CooldownElapsedPredicate cooldownElapsedPredicate,
            RematchAllowedPredicate rematchAllowedPredicate,

            MaximumPartySizePredicate.PlayerPredicate maximumPartySizePredicate,
            MinimumPartySizePredicate.PlayerPredicate minimumPartySizePredicate,

            MaximumPartyLevelPredicate maximumPartyLevelPredicate,
            MinimumPartyLevelPredicate minimumPartyLevelPredicate,

            RequiredLabelPredicate requiredLabelPredicate,
            RequiredPokemonPredicate requiredPokemonPredicate,
            RequiredHeldItemPredicate requiredHeldItemPredicate,
            RequiredAbilityPredicate requiredAbilityPredicate,
            RequiredMovePredicate requiredMovePredicate,

            ForbiddenLabelPredicate forbiddenLabelPredicate,
            ForbiddenPokemonPredicate forbiddenPokemonPredicate,
            ForbiddenHeldItemPredicate forbiddenHeldItemPredicate,
            ForbiddenAbilityPredicate forbiddenAbilityPredicate,
            ForbiddenMovePredicate forbiddenMovePredicate
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

        this.isSpawningAllowed = isSpawningAllowed;

        this.cooldownElapsedPredicate = cooldownElapsedPredicate;
        this.rematchAllowedPredicate = rematchAllowedPredicate;

        this.maximumPartySizePredicate = maximumPartySizePredicate;
        this.minimumPartySizePredicate = minimumPartySizePredicate;
        this.maximumPartyLevelPredicate = maximumPartyLevelPredicate;
        this.minimumPartyLevelPredicate = minimumPartyLevelPredicate;

        this.requiredLabelPredicate = requiredLabelPredicate;
        this.requiredPokemonPredicate = requiredPokemonPredicate;
        this.requiredHeldItemPredicate = requiredHeldItemPredicate;
        this.requiredAbilityPredicate = requiredAbilityPredicate;
        this.requiredMovePredicate = requiredMovePredicate;

        this.forbiddenLabelPredicate = forbiddenLabelPredicate;
        this.forbiddenPokemonPredicate = forbiddenPokemonPredicate;
        this.forbiddenHeldItemPredicate = forbiddenHeldItemPredicate;
        this.forbiddenAbilityPredicate = forbiddenAbilityPredicate;
        this.forbiddenMovePredicate = forbiddenMovePredicate;
    }

    public List<PokemonLevelPair> getTeam() {
        return team;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Text getDisplayName() {
        return displayName;
    }

    public LevelMode getLevelMode() {
        return levelMode;
    }

    public BattleFormat getBattleFormat() {
        return battleFormat;
    }

    public BattleAI getBattleAI() {
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

    // TODO rename to isSpawnAllowed
    public boolean isSpawningAllowed() {
        return isSpawningAllowed;
    }

    public CooldownElapsedPredicate getCooldownElapsedPredicate() {
        return cooldownElapsedPredicate;
    }

    public RematchAllowedPredicate getRematchAllowedPredicate() {
        return rematchAllowedPredicate;
    }

    public MaximumPartySizePredicate.PlayerPredicate getMaximumPartySizePredicate() {
        return maximumPartySizePredicate;
    }

    public MinimumPartySizePredicate.PlayerPredicate getMinimumPartySizePredicate() {
        return minimumPartySizePredicate;
    }

    public MaximumPartyLevelPredicate getMaximumPartyLevelPredicate() {
        return maximumPartyLevelPredicate;
    }

    public MinimumPartyLevelPredicate getMinimumPartyLevelPredicate() {
        return minimumPartyLevelPredicate;
    }

    public RequiredLabelPredicate getRequiredLabelPredicate() {
        return requiredLabelPredicate;
    }

    public RequiredPokemonPredicate getRequiredPokemonPredicate() {
        return requiredPokemonPredicate;
    }

    public RequiredHeldItemPredicate getRequiredHeldItemPredicate() {
        return requiredHeldItemPredicate;
    }

    public RequiredAbilityPredicate getRequiredAbilityPredicate() {
        return requiredAbilityPredicate;
    }

    public RequiredMovePredicate getRequiredMovePredicate() {
        return requiredMovePredicate;
    }

    public ForbiddenLabelPredicate getForbiddenLabelPredicate() {
        return forbiddenLabelPredicate;
    }

    public ForbiddenPokemonPredicate getForbiddenPokemonPredicate() {
        return forbiddenPokemonPredicate;
    }

    public ForbiddenHeldItemPredicate getForbiddenHeldItemPredicate() {
        return forbiddenHeldItemPredicate;
    }

    public ForbiddenAbilityPredicate getForbiddenAbilityPredicate() {
        return forbiddenAbilityPredicate;
    }

    public ForbiddenMovePredicate getForbiddenMovePredicate() {
        return forbiddenMovePredicate;
    }
}