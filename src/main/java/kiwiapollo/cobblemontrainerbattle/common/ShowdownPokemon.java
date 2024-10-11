package kiwiapollo.cobblemontrainerbattle.common;

import java.util.List;
import java.util.Map;

@Deprecated
public class ShowdownPokemon {
    public String name;
    public String species;
    public String item;
    public String ability;
    public String gender;
    public String nature;
    public int level;
    public Map<String, Integer> evs;
    public Map<String, Integer> ivs;
    public List<String> moves;

    public ShowdownPokemon(
            String name,
            String species,
            String item,
            String ability,
            String gender,
            String nature,
            int level,
            Map<String, Integer> evs,
            Map<String, Integer> ivs,
            List<String> moves) {
        this.name = name;
        this.species = species;
        this.item = item;
        this.ability = ability;
        this.gender = gender;
        this.nature = nature;
        this.level = level;
        this.evs = evs;
        this.ivs = ivs;
        this.moves = moves;
    }
}
