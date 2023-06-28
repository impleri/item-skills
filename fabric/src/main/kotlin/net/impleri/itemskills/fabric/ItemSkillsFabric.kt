package net.impleri.itemskills.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.impleri.itemskills.ItemSkills
import net.impleri.itemskills.integrations.trinkets.TrinketsSkills

class ItemSkillsFabric : ModInitializer {
  override fun onInitialize() {
    ItemSkills.init()
    registerTrinkets()
  }

  private fun registerTrinkets() {
    if (FabricLoader.getInstance().isModLoaded("trinkets")) {
      ServerTickEvents.START_SERVER_TICK.register(
        ServerTickEvents.StartTick { TrinketsSkills.onServerTick(it) },
      )
    }
  }
}
