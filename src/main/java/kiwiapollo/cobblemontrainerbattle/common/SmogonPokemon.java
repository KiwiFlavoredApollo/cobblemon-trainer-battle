package kiwiapollo.cobblemontrainerbattle.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmogonPokemon {
    public String name;
    public String species;
    public String item;
    public String abiltiy;
    public String gender;
    public String nature;
    public Map<String, Integer> evs = new HashMap<>();
    public Map<String, Integer> ivs = new HashMap<>();
    public int level = 0;
    public List<String> moves = new ArrayList<>();
}
