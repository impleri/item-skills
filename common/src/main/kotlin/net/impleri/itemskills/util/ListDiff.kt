package net.impleri.itemskills.util

import net.impleri.itemskills.api.ItemRestriction
import net.minecraft.world.item.Item

object ListDiff {
  fun contains(a: List<Item>, b: List<Item>): Boolean {
    return getMissing(a, b).isEmpty()
  }

  fun getMissing(a: List<Item>, b: List<Item>): List<Item> {
    if (a.isEmpty() && b.isEmpty()) {
      return ArrayList()
    }

    val bStrings = b.map { ItemRestriction.getName(it) }

    return a.filter { !bStrings.contains(ItemRestriction.getName(it)) }
  }
}
