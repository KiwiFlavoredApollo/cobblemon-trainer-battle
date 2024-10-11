package kiwiapollo.cobblemontrainerbattle.economies;


import kiwiapollo.cobblemontrainerbattle.exceptions.EconomyNotLoadedException;

public class EconomyFactory {
    public static Economy create(String identifier) {
        try {
            return switch (identifier) {
                case "OctoEconomy" -> new OctoEconomy();
                default -> new NullEconomy();
            };

        } catch (EconomyNotLoadedException e) {
            return new NullEconomy();
        }
    }
}
