package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

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
