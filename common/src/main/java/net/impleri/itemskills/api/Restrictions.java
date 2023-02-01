package net.impleri.itemskills.api;

import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.api.RestrictionsApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;

public class Restrictions extends RestrictionsApi<Restriction> {
    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    public static final Restrictions INSTANCE = new Restrictions(Registry.INSTANCE, allRestrictionFields);

    private Restrictions(net.impleri.playerskills.restrictions.Registry<Restriction> registry, Field[] fields) {
        super(registry, fields);
    }

    public boolean isProducible(Player player, ResourceLocation item) {
        return canPlayer(player, item, "producible");
    }

    public boolean isConsumable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "consumable");
    }

    public boolean isHoldable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "holdable");
    }

    public boolean isIdentifiable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "identifiable");
    }

    public boolean isHarmful(Player player, ResourceLocation item) {
        return canPlayer(player, item, "harmful");
    }

    public boolean isWearable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "wearable");
    }

    public boolean isUsable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "usable");
    }
}
