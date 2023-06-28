package net.impleri.itemskills.integrations.kubejs.events

import dev.latvian.mods.rhino.util.HideFromJS
import net.impleri.itemskills.restrictions.ItemConditions
import net.impleri.itemskills.restrictions.Restriction
import net.impleri.playerskills.integrations.kubejs.api.AbstractRestrictionConditionsBuilder
import net.impleri.playerskills.integrations.kubejs.api.PlayerDataJS
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.Item

class RestrictionConditionsBuilderJS @HideFromJS constructor(
  server: Lazy<MinecraftServer>,
) : AbstractRestrictionConditionsBuilder<Item, Restriction>(server), ItemConditions<PlayerDataJS> {
  override var replacement: Item? = null
  override var producible: Boolean? = false
  override var consumable: Boolean? = false
  override var holdable: Boolean? = false
  override var identifiable: Boolean? = false
  override var harmful: Boolean? = false
  override var wearable: Boolean? = false
  override var usable: Boolean? = false
}
