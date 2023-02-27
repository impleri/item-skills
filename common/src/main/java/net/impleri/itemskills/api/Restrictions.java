package net.impleri.itemskills.api;

import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class Restrictions extends RestrictionsApi<Item, Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    public static final Restrictions INSTANCE = new Restrictions(Registry.INSTANCE, allRestrictionFields);

    private Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    @Override
    protected ResourceLocation getTargetName(Item target) {
        return ItemHelper.getItemKey(target);
    }

    @Override
    protected Predicate<Item> createPredicateFor(Item item) {
        return (Item target) -> target == item;
    }

    public boolean isProducible(Player player, Item item) {
        return canPlayer(player, item, "producible");
    }

    public boolean isConsumable(Player player, Item item) {
        return canPlayer(player, item, "consumable");
    }

    public boolean isHoldable(Player player, Item item) {
        return canPlayer(player, item, "holdable");
    }

    public boolean isIdentifiable(Player player, Item item) {
        return canPlayer(player, item, "identifiable");
    }

    public boolean isHarmful(Player player, Item item) {
        return canPlayer(player, item, "harmful");
    }

    public boolean isWearable(Player player, Item item) {
        return canPlayer(player, item, "wearable");
    }

    public boolean isUsable(Player player, Item item) {
        return canPlayer(player, item, "usable");
    }
}
