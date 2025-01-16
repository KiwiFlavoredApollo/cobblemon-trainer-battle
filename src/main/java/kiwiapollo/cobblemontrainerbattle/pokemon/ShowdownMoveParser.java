package kiwiapollo.cobblemontrainerbattle.pokemon;

import java.util.Map;

public class ShowdownMoveParser {
    private static final Map<String, String> EXCEPTION = Map.ofEntries(
            Map.entry("Drain Kiss", "drainingkiss"),
            Map.entry("Bad Tantrum", "stompingtantrum"),
            Map.entry("Dark Hole", "darkvoid"),
            Map.entry("Para Charge", "paraboliccharge"),
            Map.entry("HiHorsepower", "highhorsepower"),
            Map.entry("Expand Force", "expandingforce"),
            Map.entry("Teary Look", "tearfullook"),
            Map.entry("Mist Terrain", "mistyterrain"),
            Map.entry("Draco Hammer", "dragonhammer"),
            Map.entry("Hi Jump Kick", "highjumpkick"),
            Map.entry("Dazzle Gleam", "dazzlinggleam"),
            Map.entry("King's Shield", "kingsshield"),
            Map.entry("Psychic Fang", "psychicfangs"),
            Map.entry("Disarm Cry", "disarmingvoice"),
            Map.entry("SurgeStrikes", "surgingstrikes"),
            Map.entry("SmellingSalt", "smellingsalts"),
            Map.entry("Nature's Madness", "naturesmadness"),
            Map.entry("Clang Scales", "clangingscales"),
            Map.entry("Dark Lariat", "darkestlariat"),
            Map.entry("TrickoTreat", "trickortreat"),

            Map.entry("Aqua Fang", "aquajet")
    );

    public String toCobblemonMove(String move) {
        if (EXCEPTION.containsKey(move)) {
            return EXCEPTION.get(move);
        }

        if (move.matches("Hidden Power .+")) {
            return "hiddenpower";
        }

        return normalizeMoveName(move);
    }

    private String normalizeMoveName(String moveName) {
        return moveName.replace(" ", "")
                .replace("-", "")
                .replace(",", "")
                .toLowerCase();
    }
}
