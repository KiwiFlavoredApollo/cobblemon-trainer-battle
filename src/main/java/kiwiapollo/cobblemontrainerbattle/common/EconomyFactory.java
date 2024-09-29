package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.fcgymbadges.economies.Economy;
import kiwiapollo.fcgymbadges.economies.NullEconomy;
import kiwiapollo.fcgymbadges.economies.OctoEconomy;
import kiwiapollo.fcgymbadges.economies.VanillaEconomy;
import kiwiapollo.fcgymbadges.exceptions.EconomyNotLoadedException;
import kiwiapollo.fcgymbadges.exceptions.InvalidCurrencyAmountException;
import kiwiapollo.fcgymbadges.exceptions.InvalidVanillaCurrencyItemException;

public class EconomyFactory {
    public static Economy create(String identifier) {
        try {
            return switch (identifier) {
                case "Vanilla" -> new VanillaEconomy();
                case "OctoEconomy" -> new OctoEconomy();
                default -> new NullEconomy();
            };

        } catch (
                InvalidVanillaCurrencyItemException
                | InvalidCurrencyAmountException
                | EconomyNotLoadedException e
        ) {
            return new NullEconomy();
        }
    }
}
