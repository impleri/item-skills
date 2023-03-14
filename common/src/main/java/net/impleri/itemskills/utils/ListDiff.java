package net.impleri.itemskills.utils;

import net.impleri.itemskills.ItemHelper;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ListDiff {
    public static boolean contains(List<Item> a, List<Item> b) {
        return getMissing(a, b).size() == 0;
    }

    public static List<Item> getMissing(List<Item> a, List<Item> b) {
        if (a.size() == 0 && b.size() == 0) {
            return new ArrayList<>();
        }

        var bStrings = b.stream()
                .map(ItemHelper::getItemKey)
                .toList();

        return a.stream()
                .filter(existing -> !bStrings.contains(ItemHelper.getItemKey(existing)))
                .toList();
    }
}
