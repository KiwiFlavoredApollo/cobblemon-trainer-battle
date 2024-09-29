package kiwiapollo.cobblemontrainerbattle.economies;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.EconomyNotLoadedException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidCurrencyAmountException;
import net.minecraft.server.network.ServerPlayerEntity;

public class OctoEconomy implements Economy {
    public OctoEconomy() throws EconomyNotLoadedException, InvalidCurrencyAmountException {
        assertOctoEconomyLoaded();
        assertValidVictoryCurrencyAmount();
        assertValidDefeatCurrencyAmount();

        CobblemonTrainerBattle.LOGGER.info("Loaded OctoEconomy");
    }

    private void assertValidVictoryCurrencyAmount() throws InvalidCurrencyAmountException {
        if (CobblemonTrainerBattle.CONFIG.victoryCurrencyAmount < 0) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format(
                            "Invalid value set to victoryCurrencyAmount: %f",
                            CobblemonTrainerBattle.CONFIG.victoryCurrencyAmount
                    )
            );

            CobblemonTrainerBattle.LOGGER.error("Failed to load OctoEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertValidDefeatCurrencyAmount() throws InvalidCurrencyAmountException {
        if (CobblemonTrainerBattle.CONFIG.defeatCurrencyAmount < 0) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format(
                            "Invalid value set to defeatCurrencyAmount: %f",
                            CobblemonTrainerBattle.CONFIG.defeatCurrencyAmount
                    )
            );

            CobblemonTrainerBattle.LOGGER.error("Failed to load OctoEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertOctoEconomyLoaded() throws EconomyNotLoadedException {
        try {
            Class.forName("com.epherical.octoecon.OctoEconomy");

        } catch (ClassNotFoundException e) {
            CobblemonTrainerBattle.LOGGER.error("Failed to load OctoEconomy");
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
        return String.format("Not enough balance");
    }
}
