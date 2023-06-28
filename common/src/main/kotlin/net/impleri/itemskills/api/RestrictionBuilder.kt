package net.impleri.itemskills.api

import net.impleri.itemskills.ItemSkills
import net.impleri.itemskills.restrictions.ItemConditions
import net.impleri.itemskills.restrictions.Restriction
import net.impleri.playerskills.restrictions.AbstractRestrictionBuilder
import net.impleri.playerskills.restrictions.RestrictionConditionsBuilder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class RestrictionBuilder : AbstractRestrictionBuilder<Item, Restriction>(
  Registry.ITEM,
  ItemSkills.LOGGER,
) {
  override fun <Player> restrictOne(
    name: ResourceLocation,
    builder: RestrictionConditionsBuilder<Item, Player, Restriction>,
  ) {
  }

  override fun getName(resource: Item): ResourceLocation {
    return ItemRestriction.getName(resource)
  }

  override fun isTagged(tag: TagKey<Item>): (Item) -> Boolean {
    return { it.defaultInstance.`is`(tag) }
  }

  companion object {
    private val instance = RestrictionBuilder()

    fun <Player> register(name: String, builder: ItemConditions<Player>) {
      instance.create(name, builder)
    }
  }
}
