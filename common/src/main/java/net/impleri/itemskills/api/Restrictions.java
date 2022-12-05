package net.impleri.itemskills.api;

import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.itemskills.restrictions.Restriction;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public abstract class Restrictions {
    private static Player getPlayer() {
        try {
            return Minecraft.getInstance().player;
        } catch (Throwable ignored) {}

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
        ItemSkills.LOGGER.info(
                "Restriction for {} values: craftable = {}. visible = {}. holdable = {}. identifiable = {}. harmful = {}. wearable = {}. usable = {}.",
                restriction.item,
                restriction.craftable,
                restriction.visible,
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
        } catch (IllegalAccessException | IllegalArgumentException | NullPointerException | ExceptionInInitializerError ignored) {}

        // default to allow if we get some error when trying to access the field
        return true;
    }

    private static boolean canPlayer(Player player, ResourceLocation item, String fieldName) {
        boolean hasRestrictions = Registry.find(item).stream()
                .peek(r -> ItemSkills.LOGGER.info("All restriction {}", r.item))
                .filter(restriction -> restriction.condition.apply(player)) // reduce to those whose condition matches the player
                .peek(r -> ItemSkills.LOGGER.info("Matched restriction {}", r.item))
                .map(restriction -> getFieldValueFor(restriction, fieldName)) // get field value
                .anyMatch(value -> !value); // do we have any restrictions that deny the action

        ItemSkills.LOGGER.info("Does {} for {} have {} restrictions? {}", item, player.getName().getString(), fieldName, hasRestrictions);

        return !hasRestrictions;
    }

    private static boolean canPlayer(ResourceLocation item, String fieldName) {
        Player player = getPlayer();

        if (player == null) {
            ItemSkills.LOGGER.warn("Attempted to determine if null player can {} on {}", fieldName, item);
            return false;
        }

        return canPlayer(player, item, fieldName);
    }

    public static boolean isCraftable(ResourceLocation item) {
        return canPlayer(item, "craftable");
    }

    public static boolean isCraftable(Player player, ResourceLocation item) {
        return canPlayer(player, item, "craftable");
    }

    public static boolean isVisible(ResourceLocation item) {
        return canPlayer(item, "visible");
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

    public static boolean isHarmful(ResourceLocation item) {
        return canPlayer(item, "harmful");
    }
    public static boolean isHarmful(Player player, ResourceLocation item) {
        return canPlayer(player, item, "harmful");
    }

    public static boolean isWearable(ResourceLocation item) {
        return canPlayer(item, "wearable");
    }
    public static boolean isUsable(ResourceLocation item) {
        return canPlayer(item, "usable");
    }
}
