package kiwiapollo.cobblemontrainerbattle.common;


import kiwiapollo.cobblemontrainerbattle.economies.Economy;
import kiwiapollo.cobblemontrainerbattle.economies.NullEconomy;
import kiwiapollo.cobblemontrainerbattle.economies.OctoEconomy;
import kiwiapollo.cobblemontrainerbattle.exceptions.EconomyNotLoadedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidCurrencyAmountException;

public class EconomyFactory {
    public static Economy create(String identifier) {
        try {
            return switch (identifier) {
                case "OctoEconomy" -> new OctoEconomy();
                default -> new NullEconomy();
            };

        } catch (InvalidCurrencyAmountException | EconomyNotLoadedException e
        ) {
            return new NullEconomy();
        }
    }
}
