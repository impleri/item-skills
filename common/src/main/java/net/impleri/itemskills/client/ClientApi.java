package net.impleri.itemskills.client;

import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class ClientApi {
    private static Player getPlayer() {
        try {
            return Minecraft.getInstance().player;
        } catch (Throwable ignored) {
        }

        return null;
    }

    public static List<Restriction> getAll() {
        return Registry.entries();
    }

    public static List<ResourceLocation> getHidden() {
        var player = getPlayer();
        return getAll().stream()
                .filter(r -> !r.producible && !r.consumable && r.condition.test(player))
                .map(r -> r.item)
                .toList();
    }

    public static List<ResourceLocation> getUnproducible() {
        var player = getPlayer();
        return getAll().stream()
                .filter(r -> !r.producible && r.condition.test(player))
                .map(r -> r.item)
                .toList();
    }

    public static List<ResourceLocation> getUnconsumable() {
        var player = getPlayer();
        return getAll().stream()
                .filter(r -> !r.consumable && r.condition.test(player))
                .map(r -> r.item)
                .toList();
    }

    public static boolean isProducible(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isProducible(player, item);
    }

    public static boolean isConsumable(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isConsumable(player, item);
    }

    public static boolean isHoldable(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isHoldable(player, item);
    }

    public static boolean isIdentifiable(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isIdentifiable(player, item);
    }

    public static boolean isHarmful(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isHarmful(player, item);
    }

    public static boolean isWearable(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isWearable(player, item);
    }

    public static boolean isUsable(ResourceLocation item) {
        var player = getPlayer();
        return Restrictions.isUsable(player, item);
    }

}
