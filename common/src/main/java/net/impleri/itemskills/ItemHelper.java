package net.impleri.itemskills;

import net.impleri.itemskills.restrictions.Restrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

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

    public static boolean isHarmful(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isHarmful(player, item, pos);
    }

    public static boolean isHoldable(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isHoldable(player, item, pos);
    }

    public static boolean isUsable(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isUsable(player, item, pos);
    }

    public static boolean isWearable(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isWearable(player, item, pos);
    }

    public static boolean isProducible(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isProducible(player, item, pos);
    }

    public static boolean isConsumable(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isConsumable(player, item, pos);
    }

    public static boolean isIdentifiable(Player player, Item item, @Nullable BlockPos pos) {
        return Restrictions.INSTANCE.isIdentifiable(player, item, pos);
    }

    public static boolean isProducible(Player player, Recipe<?> recipe, @Nullable BlockPos pos) {
        var item = recipe.getResultItem().getItem();
        ItemSkills.LOGGER.debug("Checking if {} is producible", item);

        return isProducible(player, item, pos);
    }
}
