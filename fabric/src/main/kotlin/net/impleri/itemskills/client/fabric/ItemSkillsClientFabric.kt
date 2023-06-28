package net.impleri.itemskills.client.fabric

import net.fabricmc.api.ClientModInitializer
import net.impleri.itemskills.client.ItemSkillsClient

class ItemSkillsClientFabric : ClientModInitializer {
  override fun onInitializeClient() {
    ItemSkillsClient.init()
  }
}
