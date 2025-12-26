package kiwiapollo.cobblemontrainerbattle.pokemon;

import java.util.List;
import java.util.Map;

public class ShowdownPokemon {
    public final String name;
    public final String species;
    public final String form;
    public final boolean shiny;
    public final String item;
    public final String ability;
    public final String gender;
    public final String nature;
    public final Map<String, Integer> evs;
    public final Map<String, Integer> ivs;
    public final int level;
    public final List<String> moves;

    public ShowdownPokemon() {
        this.name = "";
        this.species = "";
        this.form = "";
        this.shiny = false;
        this.item = "";
        this.ability = "";
        this.gender = "";
        this.nature = "";
        this.evs = Map.of(
                "hp", 0,
                "atk", 0,
                "def", 0,
                "spa", 0,
                "spd", 0,
                "spe", 0
        );
        this.ivs = Map.of(
                "hp", 0,
                "atk", 0,
                "def", 0,
                "spa", 0,
                "spd", 0,
                "spe", 0
        );
        this.level = 10;
        this.moves = List.of();
    }
    
    public ShowdownPokemon(
            String name,
            String species,
            String form,
            boolean shiny,
            String item,
            String ability,
            String gender,
            String nature,
            int level,
            Map<String, Integer> evs,
            Map<String, Integer> ivs,
            List<String> moves
    ) {
        this.name = name;
        this.species = species;
        this.form = form;
        this.shiny = shiny;
        this.item = item;
        this.ability = ability;
        this.gender = gender;
        this.nature = nature;
        this.evs = evs;
        this.ivs = ivs;
        this.level = level;
        this.moves = moves;
    }
}
