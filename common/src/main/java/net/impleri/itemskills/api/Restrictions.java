package net.impleri.itemskills.api;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Restrictions {
    private static Player getPlayer() {
        if (Platform.getEnvironment() != Env.CLIENT) {
            ItemSkills.LOGGER.warn("Trying to identify player from the server context");
            return null;
        }

        try {
            return Minecraft.getInstance().player;
        } catch (Throwable ignored) {
        }

        return null;
    }

    private static final Field[] allRestrictionFields = Restriction.class.getDeclaredFields();

    private static Field getField(String name) {
        Optional<Field> found = Arrays.stream(allRestrictionFields)
                .filter(field -> field.getName().equals(name))
                .findFirst();

        return found.orElse(null);
    }

    private static void logRestriction(Restriction restriction) {
        ItemSkills.LOGGER.debug(
                "Restriction for {} values: producible = {}. consumable = {}. holdable = {}. identifiable = {}. harmful = {}. wearable = {}. usable = {}.",
                restriction.item,
                restriction.producible,
                restriction.consumable,
                restriction.holdable,
                restriction.identifiable,
                restriction.harmful,
                restriction.wearable,
                restriction.usable
        );
    }

    private static boolean getFieldValueFor(Restriction restriction, String fieldName) {
        logRestriction(restriction);

        Field field = getField(fieldName);

        // default to allow if field doesn't exist (this should never happen)
        if (field == null) {
            return true;
        }

        try {
            // return the boolean value of the field
            return field.getBoolean(restriction);
        } catch (IllegalAccessException | IllegalArgumentException | NullPointerException | ExceptionInInitializerError ignored) {
        }

        // default to allow if we get some error when trying to access the field
        return true;
    }

    private static boolean canPlayer(Player player, ResourceLocation item, String fieldName) {
        if (player == null) {
            ItemSkills.LOGGER.warn("Attempted to determine if null player can {} on {}", fieldName, item);
            return false;
        }

        boolean hasRestrictions = Registry.find(item).stream()
                .filter(restriction -> restriction.condition.test(player)) // reduce to those whose condition matches the player
                .map(restriction -> getFieldValueFor(restriction, fieldName)) // get field value
                .anyMatch(value -> !value); // do we have any restrictions that deny the action

        ItemSkills.LOGGER.debug("Does {} for {} have {} restrictions? {}", item, player.getName().getString(), fieldName, hasRestrictions);

        return !hasRestrictions;
    }

    private static boolean canPlayer(ResourceLocation item, String fieldName) {
        Player player = getPlayer();

        return canPlayer(player, item, fieldName);
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
        return canPlayer(item, "producible");
    }

    public static boolean isProducible(Player player, ResourceLocation item) {
        return canPlayer(player, item, "producible");
    }

    public static boolean isConsumable(ResourceLocation item) {
        return canPlayer(item, "consumable");
    }

    public static boolean isConsumable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "consumable");
    }

    public static boolean isHoldable(ResourceLocation item) {
        return canPlayer(item, "holdable");
    }

    public static boolean isHoldable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "holdable");
    }

    public static boolean isIdentifiable(ResourceLocation item) {
        return canPlayer(item, "identifiable");
    }

    public static boolean isIdentifiable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "identifiable");
    }

    public static boolean isHarmful(ResourceLocation item) {
        return canPlayer(item, "harmful");
    }

    public static boolean isHarmful(Player player, ResourceLocation item) {
        return canPlayer(player, item, "harmful");
    }

    public static boolean isWearable(ResourceLocation item) {
        return canPlayer(item, "wearable");
    }

    public static boolean isWearable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "wearable");
    }

    public static boolean isUsable(ResourceLocation item) {
        return canPlayer(item, "usable");
    }

    public static boolean isUsable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "usable");
    }
}
