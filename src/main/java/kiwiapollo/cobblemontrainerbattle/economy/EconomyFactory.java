package kiwiapollo.cobblemontrainerbattle.economy;


import kiwiapollo.cobblemontrainerbattle.exception.EconomyNotLoadedException;

public class EconomyFactory {
    public Economy create(String identifier) {
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
