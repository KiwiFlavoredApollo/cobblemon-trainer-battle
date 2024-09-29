package kiwiapollo.cobblemontrainerbattle.economies;

import kiwiapollo.fcgymbadges.FractalCoffeeGymBadges;
import kiwiapollo.fcgymbadges.exceptions.EconomyNotLoadedException;
import kiwiapollo.fcgymbadges.exceptions.InvalidCurrencyAmountException;
import net.minecraft.server.network.ServerPlayerEntity;

public class OctoEconomy implements Economy {
    public OctoEconomy() throws EconomyNotLoadedException, InvalidCurrencyAmountException {
        assertOctoEconomyLoaded();
        assertValidCurrencyAmount();

        FractalCoffeeGymBadges.LOGGER.info("Loaded OctoEconomy");
    }

    private void assertValidCurrencyAmount() throws InvalidCurrencyAmountException {
        if (FractalCoffeeGymBadges.CONFIG.gymBadgeCreatePrice < 0) {
            FractalCoffeeGymBadges.LOGGER.error(
                    String.format(
                            "Invalid value set to gymBadgeCreatePrice: %f",
                            FractalCoffeeGymBadges.CONFIG.gymBadgeCreatePrice
                    )
            );

            FractalCoffeeGymBadges.LOGGER.error("Failed to load OctoEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertOctoEconomyLoaded() throws EconomyNotLoadedException {
        try {
            Class.forName("com.epherical.octoecon.OctoEconomy");

        } catch (ClassNotFoundException e) {
            FractalCoffeeGymBadges.LOGGER.error("Failed to load OctoEconomy");
            throw new EconomyNotLoadedException();
        }
    }

    @Override
    public double getBalance(ServerPlayerEntity player) {
        com.epherical.octoecon.OctoEconomy octoEconomy = com.epherical.octoecon.OctoEconomy.getInstance();
        return octoEconomy.getCurrentEconomy().getOrCreatePlayerAccount(player.getUuid())
                .getBalance(octoEconomy.getCurrentEconomy().getDefaultCurrency());
    }

    @Override
    public void addBalance(ServerPlayerEntity player, double amount) {
        com.epherical.octoecon.OctoEconomy octoEconomy = com.epherical.octoecon.OctoEconomy.getInstance();
        octoEconomy.getCurrentEconomy().getOrCreatePlayerAccount(player.getUuid())
                .depositMoney(octoEconomy.getCurrentEconomy().getDefaultCurrency(), amount, "");
    }

    @Override
    public void removeBalance(ServerPlayerEntity player, double amount) {
        com.epherical.octoecon.OctoEconomy octoEconomy = com.epherical.octoecon.OctoEconomy.getInstance();
        octoEconomy.getCurrentEconomy().getOrCreatePlayerAccount(player.getUuid())
                .withdrawMoney(octoEconomy.getCurrentEconomy().getDefaultCurrency(), amount, "");
    }

    @Override
    public boolean isExistEnoughBalance(ServerPlayerEntity player, double amount) {
        com.epherical.octoecon.OctoEconomy octoEconomy = com.epherical.octoecon.OctoEconomy.getInstance();
        return octoEconomy.getCurrentEconomy().getOrCreatePlayerAccount(player.getUuid())
                .hasAmount(octoEconomy.getCurrentEconomy().getDefaultCurrency(), amount);
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return String.format(
                "Not enough balance: $%.2f",
                FractalCoffeeGymBadges.CONFIG.gymBadgeCreatePrice
        );
    }
}
