package net.impleri.itemskills;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

class InventoryHelper {
    private static Predicate<ItemStack> shouldRemoveItem(Player player, boolean wearable) {
        return (ItemStack stack) -> {
            if (!stack.isEmpty()) {
                var item = ItemHelper.getItem(stack);

                var isHoldable = ItemHelper.isHoldable(player, item);

                var isWearable = !wearable || ItemHelper.isWearable(player, item);

                return !isHoldable || !isWearable;
            }

            return false;
        };
    }

    public static List<ItemStack> getItemsToRemove(Player player, NonNullList<ItemStack> inventory) {
        return inventory.stream()
                .filter(shouldRemoveItem(player, false))
                .toList();
    }

    private static void removeFromEquippedSlot(Player player, NonNullList<ItemStack> list, ItemStack stack, int index) {
        ItemSkills.LOGGER.debug("{} should not have {} equipped", player.getName().getString(), ItemHelper.getItemKey(stack));

        list.set(index, ItemStack.EMPTY);
        player.getInventory().placeItemBackInInventory(stack);
    }

    public static void filterFromList(Player player, NonNullList<ItemStack> list) {
        var predicate = shouldRemoveItem(player, true);

        for (int i = 0; i < list.size(); ++i) {
            var stack = list.get(i);
            if (predicate.test(stack)) {
                removeFromEquippedSlot(player, list, stack, i);
            }
        }
    }

    public static Consumer<ItemStack> dropFromInventory(Player player) {
        return (ItemStack stack) -> {
            ItemSkills.LOGGER.debug("{} should not be holding {}", player.getName().getString(), ItemHelper.getItemKey(stack));
            player.getInventory().removeItem(stack);
            player.drop(stack, true);
        };
    }
}
