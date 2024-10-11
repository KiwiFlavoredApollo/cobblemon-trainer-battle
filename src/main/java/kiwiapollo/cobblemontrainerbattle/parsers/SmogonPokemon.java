package kiwiapollo.cobblemontrainerbattle.parsers;

import java.util.List;
import java.util.Map;

public class SmogonPokemon {
    public String name;
    public String species;
    public String item;
    public String ability;
    public String gender;
    public String nature;
    public Map<String, Integer> evs;
    public Map<String, Integer> ivs;
    public int level;
    public List<String> moves;

    public SmogonPokemon(
            String name,
            String species,
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
