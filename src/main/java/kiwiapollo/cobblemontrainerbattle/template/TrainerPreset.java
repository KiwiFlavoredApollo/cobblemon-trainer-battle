package kiwiapollo.cobblemontrainerbattle.template;

import com.google.gson.annotations.SerializedName;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;

import java.util.List;

public class TrainerPreset {
    @SerializedName("team")
    public final String team;

    @SerializedName("display_name")
    public final String displayName;
    @SerializedName("level_mode")
    public final String levelMode;
    @SerializedName("battle_format")
    public final String battleFormat;
    @SerializedName("battle_ai")
    public final String battleAI;
    @SerializedName("battle_theme")
    public final String battleTheme;
    @SerializedName("entity_uuid")
    public final String entityUuid;
    @SerializedName("texture")
    public final String texture;

    @SerializedName("on_victory_commands")
    public final List<String> onVictoryCommands;
    @SerializedName("on_defeat_commands")
    public final List<String> onDefeatCommands;

    @SerializedName("cooldown_in_seconds")
    public final long cooldownInSeconds;
    @SerializedName(value = "is_spawn_allowed", alternate = {"is_spawning_allowed"})
    public final boolean isSpawnAllowed;
    @SerializedName("is_rematch_allowed")
    public final boolean isRematchAllowed;

    @SerializedName("maximum_party_size")
    public final int maximumPartySize;
    @SerializedName("minimum_party_size")
    public final int minimumPartySize;

    @SerializedName("maximum_party_level")
    public final int maximumPartyLevel;
    @SerializedName("minimum_party_level")
    public final int minimumPartyLevel;

    @SerializedName("required_type")
    public final List<String> requiredType;
    @SerializedName("required_label")
    public final List<String> requiredLabel;
    @SerializedName("required_pokemon")
    public final List<ShowdownPokemon> requiredPokemon;
    @SerializedName("required_held_item")
    public final List<String> requiredHeldItem;
    @SerializedName("required_ability")
    public final List<String> requiredAbility;
    @SerializedName("required_move")
    public final List<String> requiredMove;

    @SerializedName("forbidden_type")
    public final List<String> forbiddenType;
    @SerializedName("forbidden_label")
    public final List<String> forbiddenLabel;
    @SerializedName("forbidden_pokemon")
    public final List<ShowdownPokemon> forbiddenPokemon;
    @SerializedName("forbidden_held_item")
    public final List<String> forbiddenHeldItem;
    @SerializedName("forbidden_ability")
    public final List<String> forbiddenAbility;
    @SerializedName("forbidden_move")
    public final List<String> forbiddenMove;

    @SerializedName("allowed_type")
    public final List<String> allowedType;
    @SerializedName("allowed_label")
    public final List<String> allowedLabel;
    @SerializedName("allowed_pokemon")
    public final List<ShowdownPokemon> allowedPokemon;
    @SerializedName("allowed_held_item")
    public final List<String> allowedHeldItem;
    @SerializedName("allowed_ability")
    public final List<String> allowedAbility;
    @SerializedName("allowed_move")
    public final List<String> allowedMove;

    @SerializedName("per_pokemon_required_type")
    public final List<String> perPokemonRequiredType;
    @SerializedName("per_pokemon_required_label")
    public final List<String> perPokemonRequiredLabel;
    @SerializedName("per_pokemon_required_pokemon")
    public final List<String> perPokemonRequiredHeldItem;
    @SerializedName("per_pokemon_required_ability")
    public final List<String> perPokemonRequiredAbility;
    @SerializedName("per_pokemon_required_move")
    public final List<String> perPokemonRequiredMove;

    public TrainerPreset() {
        this.team = null;

        this.displayName = null;
        this.levelMode = "normal";
        this.battleFormat = "single";
        this.battleAI = "generation5";
        this.battleTheme = "cobblemon:battle.pvn.default";
        this.cooldownInSeconds = 0;
        this.entityUuid = null;
        this.texture = "cobblemontrainerbattle:textures/entity/trainer/slim/red_piikapiika.png";

        this.isSpawnAllowed = false;
        this.isRematchAllowed = true;

        this.maximumPartySize = 6;
        this.minimumPartySize = 1;

        this.maximumPartyLevel = 100;
        this.minimumPartyLevel = 1;

        this.requiredType = List.of();
        this.requiredLabel = List.of();
        this.requiredPokemon = List.of();
        this.requiredHeldItem = List.of();
        this.requiredAbility = List.of();
        this.requiredMove = List.of();

        this.forbiddenType = List.of();
        this.forbiddenLabel = List.of();
        this.forbiddenPokemon = List.of();
        this.forbiddenHeldItem = List.of();
        this.forbiddenAbility = List.of();
        this.forbiddenMove = List.of();

        this.allowedType = List.of();
        this.allowedLabel = List.of();
        this.allowedPokemon = List.of();
        this.allowedHeldItem = List.of();
        this.allowedAbility = List.of();
        this.allowedMove = List.of();

        this.perPokemonRequiredType = List.of();
        this.perPokemonRequiredLabel = List.of();
        this.perPokemonRequiredHeldItem = List.of();
        this.perPokemonRequiredAbility = List.of();
        this.perPokemonRequiredMove = List.of();

        this.onVictoryCommands = List.of();
        this.onDefeatCommands = List.of();
    }
}
