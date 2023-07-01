package net.impleri.itemskills.integrations.rei

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule
import me.shedaniel.rei.api.common.util.EntryStacks
import net.impleri.itemskills.ItemSkills
import net.impleri.itemskills.client.ItemRestrictionClient
import net.impleri.itemskills.util.ListDiff
import net.impleri.playerskills.events.ClientSkillsUpdatedEvent
import net.minecraft.world.item.Item

object ItemSkillsFiltering {
  private var filteringRule: BasicFilteringRule.MarkDirty? = null
  private val currentlyFiltered: MutableList<Item> = ArrayList()
  fun updateHidden(event: ClientSkillsUpdatedEvent) {
    ItemSkills.LOGGER.debug("Client skills list has been updated: ${event.next}")
    val nextHidden: List<Item> = ItemRestrictionClient.hidden

    // Nothing on either list, so don't bother
    if (currentlyFiltered.isEmpty() && nextHidden.isEmpty()) {
      ItemSkills.LOGGER.debug("No changes in restrictions")
      return
    }

    val toHide = !ListDiff.contains(currentlyFiltered, nextHidden)
    val toReveal = !ListDiff.contains(nextHidden, currentlyFiltered)
    if ((toHide || toReveal) && filteringRule != null) {
      ItemSkills.LOGGER.debug("Updating REI filters")
      // Update what we're supposed
      currentlyFiltered.clear()
      currentlyFiltered.addAll(nextHidden)

      // Trigger re-filtering REI entry stacks
      filteringRule?.markDirty()
    }
  }

  fun register(rule: BasicFilteringRule<*>) {
    filteringRule = rule.hide {
      ItemSkills.LOGGER.debug("Updating REI filters")

      currentlyFiltered.map { EntryStacks.of(it) }
    }
  }
}
