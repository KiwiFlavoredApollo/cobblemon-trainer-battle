package kiwiapollo.cobblemontrainerbattle.economies;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidCurrencyAmountException;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidVanillaCurrencyItemException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class VanillaEconomy implements Economy {
    private static final Item currencyItem =
            Registries.ITEM.get(new Identifier(CobblemonTrainerBattle.CONFIG.vanillaCurrencyItem));

    public VanillaEconomy() throws InvalidVanillaCurrencyItemException, InvalidCurrencyAmountException {
        assertValidCurrencyItem();
        assertValidVictoryCurrencyAmount();
        assertValidDefeatCurrencyAmount();

        CobblemonTrainerBattle.LOGGER.info("Loaded VanillaEconomy");
    }

    private void assertValidVictoryCurrencyAmount() throws InvalidCurrencyAmountException {
        int victoryCurrencyItemCount = (int) Math.floor(CobblemonTrainerBattle.CONFIG.victoryCurrencyAmount);
        if (victoryCurrencyItemCount < 0) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format(
                            "Invalid value set to victoryCurrencyItemCount: %d",
                            victoryCurrencyItemCount
                    )
            );

            CobblemonTrainerBattle.LOGGER.error("Failed to load VanillaEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertValidDefeatCurrencyAmount() throws InvalidCurrencyAmountException {
        int defeatCurrencyItemCount = (int) Math.floor(CobblemonTrainerBattle.CONFIG.defeatCurrencyAmount);
        if (defeatCurrencyItemCount < 0) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format(
                            "Invalid value set to defeaetCurrencyItemCount: %d",
                            defeatCurrencyItemCount
                    )
            );

            CobblemonTrainerBattle.LOGGER.error("Failed to load VanillaEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertValidCurrencyItem() throws InvalidVanillaCurrencyItemException {
        if (currencyItem == Items.AIR) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format(
                            "Invalid item set to vanillaCurrencyItem: %s",
                            CobblemonTrainerBattle.CONFIG.vanillaCurrencyItem
                    )
            );

            CobblemonTrainerBattle.LOGGER.error("Failed to load VanillaEconomy");
            throw new InvalidVanillaCurrencyItemException();
        }
    }

    @Override
    public double getBalance(ServerPlayerEntity player) {
        int count = 0;

        // Check the main inventory
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() == currencyItem) {
                count += stack.getCount();
            }
        }

        // Check the offhand
        ItemStack offhandStack = player.getInventory().offHand.get(0);
        if (offhandStack.getItem() == currencyItem) {
            count += offhandStack.getCount();
        }

        return count;
    }

    @Override
    public void addBalance(ServerPlayerEntity player, double amount) {
        player.getInventory().offerOrDrop(new ItemStack(currencyItem, (int) amount));
    }

    @Override
    public void removeBalance(ServerPlayerEntity player, double amount) {
        int remaining = (int) amount;

        // Iterate through the player's main inventory
        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack stack = player.getInventory().main.get(i);
            if (stack.getItem() == currencyItem) {
                int stackCount = stack.getCount();

                if (stackCount <= remaining) {
                    remaining -= stackCount;
                    player.getInventory().removeStack(i);

                } else {
                    stack.decrement(remaining);
                    remaining = 0;
                }

                if (remaining <= 0) {
                    break;
                }
            }
        }

        // Check the offhand slot if still necessary
        if (remaining <= 0) {
            return;
        }

        ItemStack offhandStack = player.getInventory().offHand.get(0);
        if (offhandStack.getItem() == currencyItem) {
            int stackCount = offhandStack.getCount();

            if (stackCount <= remaining) {
                remaining -= stackCount;
                int offHand = player.getInventory().size() - 1;
                player.getInventory().removeStack(offHand);

            } else {
                offhandStack.decrement(remaining);
                remaining = 0;
            }
        }
    }

    @Override
    public boolean isExistEnoughBalance(ServerPlayerEntity player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return String.format("Not enough currency item: %s", CobblemonTrainerBattle.CONFIG.vanillaCurrencyItem);
    }
}
