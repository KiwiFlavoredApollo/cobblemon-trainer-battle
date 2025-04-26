package kiwiapollo.cobblemontrainerbattle.pokemon;

import java.util.List;
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
            Map.entry("King’s Shield", "kingsshield"),
            Map.entry("Psychic Fang", "psychicfangs"),
            Map.entry("Disarm Cry", "disarmingvoice"),
            Map.entry("SurgeStrikes", "surgingstrikes"),
            Map.entry("SmellingSalt", "smellingsalts"),
            Map.entry("Nature's Madness", "naturesmadness"),
            Map.entry("Clang Scales", "clangingscales"),
            Map.entry("Dark Lariat", "darkestlariat"),
            Map.entry("TrickoTreat", "trickortreat"),
            Map.entry("Scorch Sands", "scorchingsands"),
            Map.entry("InfernoMarch", "infernalparade"),
            Map.entry("Vice Grip", "visegrip"),
            Map.entry("Petal Storm", "petalblizzard"),
            Map.entry("Doll Eyes", "babydolleyes"),
            Map.entry("Dbl IronBash", "doubleironbash"),
            Map.entry("Trip Arrows", "triplearrows"),
            Map.entry("Water Star", "watershuriken"),
            Map.entry("Freeze Glare", "freezingglare"),
            Map.entry("FirstImpress", "firstimpression"),
            Map.entry("Swords Dance //", "swordsdance"),
            Map.entry("Effect Toxic", "toxic"),
            Map.entry("Cease Edge", "ceaselessedge"),
            Map.entry("Spirit Lock", "spiritshackle"),
            Map.entry("Steam Pump", "steameruption"),
            Map.entry("Tacke", "tackle"),

            Map.entry("Aqua Fang", "aquajet"),
            Map.entry("DracoBarrage", "dragonrush")
    );

    private static final List<String> IGNORE =  List.of(
            "None",
            "--",
            ""
    );

    public String toCobblemonMove(String move) {
        if (EXCEPTION.containsKey(move)) {
            return EXCEPTION.get(move);
        }

        if (move.matches("Hidden Power .+")) {
            return "hiddenpower";
        }

        return normalize(move);
    }

    public boolean shouldIgnore(String move) {
        return IGNORE.contains(move);
    }

    private String normalize(String moveName) {
        return moveName.replace(" ", "")
                .replace("-", "")
                .replace(",", "")
                .toLowerCase();
    }
}
