package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.BattleFormatFactory;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrainerTemplateFactory {
    private final Identifier identifier;
    private final TrainerPreset preset;
    private final TrainerTeam team;

    public TrainerTemplateFactory(Identifier identifier, TrainerPreset preset, TrainerTeam team) {
        this.identifier = identifier;
        this.preset = preset;
        this.team = team;
    }

    public TrainerTemplate create() {
        return new TrainerTemplate(
                toPokemonLevelPair(team),

                toIdentifier(identifier),
                toDisplayName(preset.displayName),
                toLevelMode(preset.levelMode),
                toBattleFormat(preset.battleFormat),
                toBattleAI(preset.battleAI),
                toBattleTheme(preset.battleTheme),
                toTexture(preset.texture),
                toEntityUuid(preset.entityUuid),

                toOnVictoryCommands(preset.onVictoryCommands),
                toOnDefeatCommands(preset.onDefeatCommands),

                toCooldownInSeconds(preset.cooldownInSeconds),
                toIsSpawnAllowed(preset.isSpawnAllowed),
                toIsRematchAllowed(preset.isRematchAllowed),

                toMaximumPartySize(preset.maximumPartySize),
                toMinimumPartySize(preset.minimumPartySize),
                toMaximumPartyLevel(preset.maximumPartyLevel),
                toMinimumPartyLevel(preset.minimumPartyLevel),

                toRequiredType(preset.requiredType),
                toRequiredLabel(preset.requiredLabel),
                toRequiredPokemon(preset.requiredPokemon),
                toRequiredHeldItem(preset.requiredHeldItem),
                toRequiredAbility(preset.requiredAbility),
                toRequiredMove(preset.requiredMove),

                toForbiddenType(preset.forbiddenType),
                toForbiddenLabel(preset.forbiddenLabel),
                toForbiddenPokemon(preset.forbiddenPokemon),
                toForbiddenHeldItem(preset.forbiddenHeldItem),
                toForbiddenAbility(preset.forbiddenAbility),
                toForbiddenMove(preset.forbiddenMove),

                toAllowedType(preset.allowedType),
                toAllowedLabel(preset.allowedLabel),
                toAllowedPokemon(preset.allowedPokemon),
                toAllowedHeldItem(preset.allowedHeldItem),
                toAllowedAbility(preset.allowedAbility),
                toAllowedMove(preset.allowedMove)
        );
    }

    private List<PokemonLevelPair> toPokemonLevelPair(TrainerTeam team) {
        List<PokemonLevelPair> pair = new ArrayList<>();

        for (ShowdownPokemon showdown : team) {
            try {
                Pokemon pokemon = new ShowdownPokemonParser().toCobblemonPokemon(showdown);
                pair.add(new PokemonLevelPair(pokemon, showdown.level));

            } catch (PokemonParseException ignored) {

            }
        }

        return pair;
    }

    private Identifier toIdentifier(Identifier identifier) {
        return identifier;
    }

    private String toDisplayName(String displayName) {
        return displayName;
    }

    private LevelMode toLevelMode(String levelMode) {
        return new LevelMode.Factory(levelMode).create();
    }

    private String toBattleAI(String battleAI) {
        return battleAI;
    }

    private BattleFormat toBattleFormat(String battleFormat) {
        return new BattleFormatFactory(battleFormat).create();
    }

    private Identifier toDefaultedIdentifier(String identifier) {
        if (identifier.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(identifier);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, identifier);
        }
    }

    private SoundEvent toBattleTheme(String battleTheme) {
        return SoundEvent.of(toDefaultedIdentifier(battleTheme));
    }

    private Identifier toTexture(String texture) {
        return toDefaultedIdentifier(texture);
    }

    private UUID toEntityUuid(String uuid) {
        if (uuid == null) {
            return null;

        } else {
            return UUID.fromString(uuid);
        }
    }

    private List<String> toOnVictoryCommands(List<String> onVictoryCommands) {
        return onVictoryCommands;
    }

    private List<String> toOnDefeatCommands(List<String> onDefeatCommands) {
        return onDefeatCommands;
    }

    private long toCooldownInSeconds(long cooldownInSeconds) {
        return cooldownInSeconds;
    }

    private boolean toIsSpawnAllowed(boolean isSpawnAllowed) {
        return isSpawnAllowed;
    }

    private boolean toIsRematchAllowed(boolean isRematchAllowed) {
        return isRematchAllowed;
    }


    private int toMaximumPartySize(int maximumPartySize) {
        return maximumPartySize;
    }

    private int toMinimumPartySize(int minimumPartySize) {
        return minimumPartySize;
    }

    private int toMaximumPartyLevel(int maximumPartyLevel) {
        return maximumPartyLevel;
    }

    private int toMinimumPartyLevel(int minimumPartyLevel) {
        return minimumPartyLevel;
    }

    private List<PokemonType> toRequiredType(List<List<String>> requiredType) {
        return requiredType.stream().map(this::toPokemonType).toList();
    }

    private PokemonType toPokemonType(List<String> type) {
        if (type.size() == 2) {
            return new PokemonType(type.get(0), type.get(1));
        }

        if (type.size() == 1) {
            return new PokemonType(type.get(0));
        }

        throw new IllegalArgumentException();
    }

    private List<String> toRequiredLabel(List<String> requiredLabel) {
        return requiredLabel;
    }

    private List<ShowdownPokemon> toRequiredPokemon(List<ShowdownPokemon> requiredPokemon) {
        return requiredPokemon;
    }

    private List<String> toRequiredHeldItem(List<String> requiredHeldItem) {
        return requiredHeldItem;
    }

    private List<String> toRequiredAbility(List<String> requiredAbility) {
        return requiredAbility;
    }

    private List<String> toRequiredMove(List<String> requiredMove) {
        return requiredMove;
    }

    private List<PokemonType> toForbiddenType(List<List<String>> forbiddenType) {
        return forbiddenType.stream().map(this::toPokemonType).toList();
    }
    
    private List<String> toForbiddenLabel(List<String> forbiddenLabel) {
        return forbiddenLabel;
    }

    private List<ShowdownPokemon> toForbiddenPokemon(List<ShowdownPokemon> forbiddenPokemon) {
        return forbiddenPokemon;
    }

    private List<String> toForbiddenHeldItem(List<String> forbiddenHeldItem) {
        return forbiddenHeldItem;
    }

    private List<String> toForbiddenAbility(List<String> forbiddenAbility) {
        return forbiddenAbility;
    }

    private List<String> toForbiddenMove(List<String> forbiddenMove) {
        return forbiddenMove;
    }

    private List<PokemonType> toAllowedType(List<List<String>> allowedType) {
        return allowedType.stream().map(this::toPokemonType).toList();
    }

    private List<String> toAllowedLabel(List<String> allowedLabel) {
        return allowedLabel;
    }

    private List<ShowdownPokemon> toAllowedPokemon(List<ShowdownPokemon> allowedPokemon) {
        return allowedPokemon;
    }

    private List<String> toAllowedHeldItem(List<String> allowedHeldItem) {
        return allowedHeldItem;
    }

    private List<String> toAllowedAbility(List<String> allowedAbility) {
        return allowedAbility;
    }

    private List<String> toAllowedMove(List<String> allowedMove) {
        return allowedMove;
    }
}
