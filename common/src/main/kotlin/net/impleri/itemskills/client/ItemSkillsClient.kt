package net.impleri.itemskills.client

import dev.architectury.event.events.client.ClientTooltipEvent
import net.impleri.itemskills.ItemSkills
import net.impleri.itemskills.api.ItemRestriction
import net.impleri.playerskills.events.ClientSkillsUpdatedEvent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.util.concurrent.ConcurrentHashMap

class ItemSkillsClient {
  private fun registerEventHandlers() {
    // Tooltip
    ClientTooltipEvent.ITEM.register(
      ClientTooltipEvent.Item { stack: ItemStack, lines: MutableList<Component>, _: TooltipFlag ->
        beforeRenderItemTooltip(
          stack,
          lines,
        )
      },
    )

    ClientSkillsUpdatedEvent.EVENT.register { clearCache() }
  }

  private val identifiability = ConcurrentHashMap<Item, Boolean>()

  private fun clearCache() {
    identifiability.clear()
  }

  private fun populateCache(item: Item): Boolean {
    return ItemRestrictionClient.canIdentify(item)
  }

  private fun beforeRenderItemTooltip(stack: ItemStack, lines: MutableList<Component>) {
    val item = ItemRestriction.getValue(stack)
    if (!identifiability.computeIfAbsent(item) { populateCache(it) }) {
      ItemSkills.LOGGER.debug("Replacing tooltip for $item")
      lines.clear()
      lines.add(Component.translatable("message.itemskills.unknown_item").withStyle(ChatFormatting.RED))
    }
  }

  companion object {
    private val INSTANCE = ItemSkillsClient()

    fun init() {
      ItemSkills.LOGGER.info("Loaded Item Skills Client")
      INSTANCE.registerEventHandlers()
    }
  }
}
