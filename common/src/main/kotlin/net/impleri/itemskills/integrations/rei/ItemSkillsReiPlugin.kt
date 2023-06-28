package net.impleri.itemskills.integrations.rei

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import net.impleri.playerskills.events.ClientSkillsUpdatedEvent

open class ItemSkillsReiPlugin : REIClientPlugin {
  init {
    // Subscribe to client skill updates to refresh basic filtering rules
    ClientSkillsUpdatedEvent.EVENT.register {
      ItemSkillsFiltering.updateHidden(it)
    }
  }

  override fun registerDisplays(registry: DisplayRegistry) {
    registry.registerVisibilityPredicate(SkillsDisplayVisibility())
  }

  override fun registerBasicEntryFiltering(rule: BasicFilteringRule<*>) {
    ItemSkillsFiltering.register(rule)
  }
}
