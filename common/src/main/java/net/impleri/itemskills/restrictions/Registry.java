package net.impleri.itemskills.restrictions;

import net.impleri.itemskills.ItemSkills;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Registry {
    private static final Map<ResourceLocation, List<Restriction>> registry = new HashMap<>();

    public static List<Restriction> entries() {
        return registry.values().stream().flatMap(Collection::stream).toList();
    }

    public static void clear() {
        registry.clear();
    }

    public static List<Restriction> find(ResourceLocation name) {
        @Nullable List<Restriction> restrictions = registry.get(name);

        ItemSkills.LOGGER.info("Current restriction for {}: {}", name, (restrictions != null) ? restrictions.size() : 0);

        return (restrictions != null) ? restrictions.stream().toList() : new ArrayList<>();
    }

    public static void add(ResourceLocation name, Restriction restriction) {
        ItemSkills.LOGGER.info("Adding restriction for {}", name);

        List<Restriction> restrictions = find(name);
        restrictions.add(restriction);

        registry.put(name, restrictions);
    }
}
