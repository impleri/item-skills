package net.impleri.itemskills;

import net.impleri.itemskills.api.Restrictions;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

class ItemHelper {
    public static ResourceLocation getItemKey(ItemStack stack) {
        return getItemKey(stack.isEmpty() ? null : stack.getItem());
    }

    public static ResourceLocation getItemKey(Item item) {
        return Registry.ITEM.getKey(item);
    }

    public static final ResourceLocation defaultItem = getItemKey((Item) null);

    public static ItemStack getItemUsed(Player player, InteractionHand hand) {
        return (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
    }

    public static void isListWearable(Player player, NonNullList<ItemStack> list) {
        iterateList(list, player, (ResourceLocation item) -> Restrictions.INSTANCE.isWearable(player, item));
    }

    public static void isListHoldable(Player player, NonNullList<ItemStack> list) {
        iterateList(list, player, (ResourceLocation item) -> Restrictions.INSTANCE.isHoldable(player, item));
    }

    private static void iterateList(NonNullList<ItemStack> list, Player player, Predicate<ResourceLocation> isItemAllowed) {
        int i;
        for (i = 0; i < list.size(); ++i) {
            var stack = list.get(i);
            var item = getItemKey(stack);
            if (item.equals(defaultItem)) {
                return;
            }

            if (!isItemAllowed.test(item)) {
                ItemSkills.LOGGER.debug("{} should not be holding {}", player.getName().getString(), item);
                list.set(i, ItemStack.EMPTY);
                player.getInventory().placeItemBackInInventory(stack);
            }
        }
    }
}
