package net.impleri.itemskills;

import net.impleri.itemskills.api.Restrictions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class ItemHelper {
    public static ResourceLocation getItemKey(ItemStack stack) {
        return getItemKey(stack.isEmpty() ? null : stack.getItem());
    }

    public static ResourceLocation getItemKey(Item item) {
        return Registry.ITEM.getKey(item);
    }

    public static Item getItem(ResourceLocation resource) {
        return Registry.ITEM.get(resource);
    }

    public static Item getItem(ItemStack stack) {
        return stack.getItem();
    }

    public static Item getItem(ItemEntity entity) {
        return getItem(entity.getItem());
    }

    public static ItemStack getItemStack(ResourceLocation resource) {
        var item = getItem(resource);

        return new ItemStack(item);
    }

    public static boolean isEmptyItem(Item item) {
        return (item == null || item == Items.AIR);
    }

    public static Item getItemUsed(Player player, InteractionHand hand) {
        var item = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();

        return getItem(item);
    }

    public static boolean isHoldable(Player player, Item item) {
        return Restrictions.INSTANCE.isHoldable(player, item);
    }

    public static boolean isWearable(Player player, Item item) {
        return Restrictions.INSTANCE.isWearable(player, item);
    }

    public static boolean isProducible(Player player, Item item) {
        return Restrictions.INSTANCE.isProducible(player, item);
    }

    public static boolean isConsumable(Player player, Item item) {
        return Restrictions.INSTANCE.isConsumable(player, item);
    }

    public static boolean isIdentifiable(Player player, Item item) {
        return Restrictions.INSTANCE.isIdentifiable(player, item);
    }

    public static boolean isProducible(Player player, Recipe<?> recipe) {
        var item = recipe.getResultItem().getItem();
        ItemSkills.LOGGER.debug("Checking if {} is producible", item);

        return isProducible(player, item);
    }
}
