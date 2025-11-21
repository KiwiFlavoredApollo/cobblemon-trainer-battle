package kiwiapollo.cobblemontrainerbattle.global.preset;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleAIFactory;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleFormatFactory;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TrainerTemplateFactory implements SimpleFactory<TrainerTemplate> {
    private final Identifier identifier;
    private final TrainerPreset preset;
    private final TrainerTeam team;

    public TrainerTemplateFactory(Identifier identifier, TrainerPreset preset, TrainerTeam team) {
        this.identifier = identifier;
        this.preset = preset;
        this.team = team;
    }

    @Override
    public TrainerTemplate create() {
        return new TrainerTemplate(
                toPokemonLevelPair(team),

                toIdentifier(identifier),
                toDisplayName(preset.displayName, identifier),
                toLevelMode(preset.levelMode),
                toBattleFormat(preset.battleFormat),
                toBattleAI(preset.battleFormat, preset.battleAI),
                toBattleTheme(preset.battleTheme),
                toTexture(preset.texture),
                toEntityUuid(preset.entityUuid),

                toOnVictoryCommands(preset.onVictoryCommands),
                toOnDefeatCommands(preset.onDefeatCommands),

                toIsSpawningAllowed(preset.isSpawningAllowed),

                toCooldownElapsedPredicate(identifier, preset.cooldownInSeconds),
                toRematchAllowedPredicate(identifier, preset.isRematchAllowed),

                toMaximumPartySizePredicate(preset.maximumPartySize),
                toMinimumPartySizePredicate(preset.minimumPartySize),
                toMaximumPartyLevelPredicate(preset.maximumPartyLevel),
                toMinimumPartyLevelPredicate(preset.minimumPartyLevel),

                toRequiredLabelPredicate(preset.requiredLabel),
                toRequiredPokemonPredicate(preset.requiredPokemon),
                toRequiredHeldItemPredicate(preset.requiredHeldItem),
                toRequiredAbilityPredicate(preset.requiredAbility),
                toRequiredMovePredicate(preset.requiredMove),

                toForbiddenLabelPredicate(preset.forbiddenLabel),
                toForbiddenPokemonPredicate(preset.forbiddenPokemon),
                toForbiddenHeldItemPredicate(preset.forbiddenHeldItem),
                toForbiddenAbilityPredicate(preset.forbiddenAbility),
                toForbiddenMovePredicate(preset.forbiddenMove)
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

        if (pair.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return pair;
    }

    private Identifier toIdentifier(Identifier identifier) {
        return identifier;
    }

    private Text toDisplayName(String displayName, Identifier identifier) {
        try {
            return Text.translatable(Objects.requireNonNull(displayName));

        } catch (NullPointerException e) {
            return Text.translatable(identifier.toString());
        }
    }

    private LevelMode toLevelMode(String levelMode) {
        return new LevelMode.Factory(levelMode).create();
    }

    private BattleAI toBattleAI(String battleFormat, String battleAI) {
        return new BattleAIFactory(battleFormat, battleAI).create();
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

    private boolean toIsSpawningAllowed(boolean isSpawningAllowed) {
        return isSpawningAllowed;
    }

    private CooldownElapsedPredicate toCooldownElapsedPredicate(Identifier trainer, long cooldownInSeconds) {
        return new CooldownElapsedPredicate(trainer, cooldownInSeconds);
    }

    private RematchAllowedPredicate toRematchAllowedPredicate(Identifier trainer, boolean isRematchAllowed) {
        return new RematchAllowedPredicate(trainer, isRematchAllowed);
    }

    private MaximumPartySizePredicate.PlayerPredicate toMaximumPartySizePredicate(int size) {
        return new MaximumPartySizePredicate.PlayerPredicate(size);
    }

    private MinimumPartySizePredicate.PlayerPredicate toMinimumPartySizePredicate(int size) {
        return new MinimumPartySizePredicate.PlayerPredicate(size);
    }

    private MaximumPartyLevelPredicate toMaximumPartyLevelPredicate(int level) {
        return new MaximumPartyLevelPredicate(level);
    }

    private MinimumPartyLevelPredicate toMinimumPartyLevelPredicate(int level) {
        return new MinimumPartyLevelPredicate(level);
    }

    private RequiredLabelPredicate toRequiredLabelPredicate(List<String> label) {
        return new RequiredLabelPredicate(label);
    }

    private RequiredPokemonPredicate toRequiredPokemonPredicate(List<ShowdownPokemon> pokemon) {
        return new RequiredPokemonPredicate(pokemon);
    }

    private RequiredHeldItemPredicate toRequiredHeldItemPredicate(List<String> item) {
        return new RequiredHeldItemPredicate(item);
    }

    private RequiredAbilityPredicate toRequiredAbilityPredicate(List<String> ability) {
        return new RequiredAbilityPredicate(ability);
    }

    private RequiredMovePredicate toRequiredMovePredicate(List<String> move) {
        return new RequiredMovePredicate(move);
    }

    private ForbiddenLabelPredicate toForbiddenLabelPredicate(List<String> label) {
        return new ForbiddenLabelPredicate(label);
    }

    private ForbiddenPokemonPredicate toForbiddenPokemonPredicate(List<ShowdownPokemon> pokemon) {
        return new ForbiddenPokemonPredicate(pokemon);
    }

    private ForbiddenHeldItemPredicate toForbiddenHeldItemPredicate(List<String> item) {
        return new ForbiddenHeldItemPredicate(item);
    }

    private ForbiddenAbilityPredicate toForbiddenAbilityPredicate(List<String> ability) {
        return new ForbiddenAbilityPredicate(ability);
    }

    private ForbiddenMovePredicate toForbiddenMovePredicate(List<String> move) {
        return new ForbiddenMovePredicate(move);
    }
}
