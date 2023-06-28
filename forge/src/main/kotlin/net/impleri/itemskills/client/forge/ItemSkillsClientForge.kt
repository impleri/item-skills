package net.impleri.itemskills.client.forge

import net.impleri.itemskills.client.ItemSkillsClient
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@EventBusSubscriber(value = [Dist.CLIENT])
object ItemSkillsClientForge {
  @SubscribeEvent
  fun onClientInit(event: FMLClientSetupEvent?) {
    ItemSkillsClient.init()
  }
}
