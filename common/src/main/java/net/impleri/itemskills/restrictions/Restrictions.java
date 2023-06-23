package net.impleri.itemskills.restrictions;

import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.playerskills.restrictions.RestrictionsApi;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<Item, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    public static final Restrictions INSTANCE = new Restrictions(ItemSkills.RESTRICTIONS, allRestrictionFields);

    private Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields, ItemSkills.LOGGER);
    }

    @Override
    protected ResourceLocation getTargetName(Item target) {
        return ItemHelper.getItemKey(target);
    }

    @Override
    protected Predicate<Item> createPredicateFor(Item item) {
        return (Item target) -> target == item;
    }

    private boolean canHelper(Player player, Item item, @Nullable BlockPos pos, String resource) {
        if (player == null) {
            return false;
        }
        var dimension = player.getLevel().dimension().location();
        var actualPos = pos == null ? player.getOnPos() : pos;
        var biome = player.getLevel().getBiome(actualPos).unwrapKey().orElseThrow().location();

        return canPlayer(player, item, dimension, biome, resource);
    }

    public boolean isProducible(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "producible");
    }

    public boolean isConsumable(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "consumable");
    }

    public boolean isHoldable(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "holdable");
    }

    public boolean isIdentifiable(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "identifiable");
    }

    public boolean isHarmful(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "harmful");
    }

    public boolean isWearable(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "wearable");
    }

    public boolean isUsable(Player player, Item item, @Nullable BlockPos pos) {
        return canHelper(player, item, pos, "usable");
    }
}
