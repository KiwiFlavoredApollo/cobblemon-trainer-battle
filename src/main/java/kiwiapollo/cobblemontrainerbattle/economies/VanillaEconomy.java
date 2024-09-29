package kiwiapollo.cobblemontrainerbattle.economies;

import kiwiapollo.fcgymbadges.FractalCoffeeGymBadges;
import kiwiapollo.fcgymbadges.exceptions.InvalidCurrencyAmountException;
import kiwiapollo.fcgymbadges.exceptions.InvalidVanillaCurrencyItemException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class VanillaEconomy implements Economy {
    private static final Item currencyItem =
            Registries.ITEM.get(new Identifier(FractalCoffeeGymBadges.CONFIG.vanillaCurrencyItem));

    public VanillaEconomy() throws InvalidVanillaCurrencyItemException, InvalidCurrencyAmountException {
        assertValidCurrencyItem();
        assertValidCurrencyAmount();

        FractalCoffeeGymBadges.LOGGER.info("Loaded VanillaEconomy");
    }

    private void assertValidCurrencyAmount() throws InvalidCurrencyAmountException {
        int currencyItemCount = (int) Math.floor(FractalCoffeeGymBadges.CONFIG.gymBadgeCreatePrice);
        if (currencyItemCount < 0) {
            FractalCoffeeGymBadges.LOGGER.error(
                    String.format(
                            "Invalid value set to gymBadgeCreatePrice: %d",
                            currencyItemCount
                    )
            );

            FractalCoffeeGymBadges.LOGGER.error("Failed to load VanillaEconomy");
            throw new InvalidCurrencyAmountException();
        }
    }

    private void assertValidCurrencyItem() throws InvalidVanillaCurrencyItemException {
        if (currencyItem == Items.AIR) {
            FractalCoffeeGymBadges.LOGGER.error(
                    String.format(
                            "Invalid item set to vanillaCurrencyItem: %s",
                            FractalCoffeeGymBadges.CONFIG.vanillaCurrencyItem
                    )
            );

            FractalCoffeeGymBadges.LOGGER.error("Failed to load VanillaEconomy");
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
        int currencyItemCount = (int) Math.floor(FractalCoffeeGymBadges.CONFIG.gymBadgeCreatePrice);
        return String.format(
                "Not enough item: %s %d",
                Registries.ITEM.get(new Identifier(
                        FractalCoffeeGymBadges.CONFIG.vanillaCurrencyItem)).getName().getString(),
                currencyItemCount
        );
    }
}
