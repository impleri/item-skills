package net.impleri.itemskills.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ListDiff {
    public static boolean contains(List<ResourceLocation> a, List<ResourceLocation> b) {
        return getMissing(a, b).size() == 0;
    }

    public static List<ResourceLocation> getMissing(List<ResourceLocation> a, List<ResourceLocation> b) {
        if (a.size() == 0 && b.size() == 0) {
            return new ArrayList<>();
        }

        var bStrings = b.stream()
                .map(ResourceLocation::toString)
                .toList();

        var aCopy = a.stream()
                .filter(existing -> !bStrings.contains(existing.toString()))
                .toList();

        return aCopy;
    }
}
