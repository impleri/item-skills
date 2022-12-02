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

    private static boolean doesRestrictionAllow(Restriction restriction, String name) {
        Optional<Field> found = Arrays.stream(allRestrictionFields)
                .filter(field -> field.getName().equals(name))
                .findFirst();

        if (found.isEmpty()) {
            return false;
        }

        try {
            return found.get().getBoolean(restriction);
        } catch (IllegalAccessException ignored) {}

        return false;
    }

    private static boolean canPlayer(ResourceLocation item, String fieldName) {
        Player player = getPlayer();

        if (player == null) {
            ItemSkills.LOGGER.warn("Attempted to determine if null player can {} on {}", fieldName, item);
            return false;
        }

        long restrictions = Registry.find(item).stream()
                // Filter down to those which match the condition and disallow crafting
                .filter(restriction -> restriction.condition.apply(player) && !doesRestrictionAllow(restriction, fieldName))
                .count();

        var result = restrictions > 0;

        ItemSkills.LOGGER.info("Can {} craft {}? {}", player.getName().getString(), item, result);

        return result;
    }

    public static boolean isCraftable(ResourceLocation item) {
        return canPlayer(item, "craftable");
    }

    public static boolean isVisible(ResourceLocation item) {
        return canPlayer(item, "visible");
    }

    public static boolean isHoldable(ResourceLocation item) {
        return canPlayer(item, "holdable");
    }
    public static boolean isIdentifiable(ResourceLocation item) {
        return canPlayer(item, "identifiable");
    }
    public static boolean isHarmful(ResourceLocation item) {
        return canPlayer(item, "harmful");
    }
    public static boolean isWearable(ResourceLocation item) {
        return canPlayer(item, "wearable");
    }
    public static boolean isUsable(ResourceLocation item) {
        return canPlayer(item, "usable");
    }
}
