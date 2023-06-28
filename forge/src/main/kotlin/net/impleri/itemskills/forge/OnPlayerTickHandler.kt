package net.impleri.itemskills.forge

import net.impleri.itemskills.integrations.curios.CuriosSkills
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber
object OnPlayerTickHandler {
  @SubscribeEvent
  fun onPlayerTick(event: PlayerTickEvent) {
    if (event.side == LogicalSide.SERVER && ModList.get().isLoaded("curios")) {
      CuriosSkills.onPlayerTick(event.player)
    }
  }
}
