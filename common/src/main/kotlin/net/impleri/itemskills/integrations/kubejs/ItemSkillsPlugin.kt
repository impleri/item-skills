package net.impleri.itemskills.integrations.kubejs

import dev.architectury.event.events.common.LifecycleEvent
import dev.latvian.mods.kubejs.KubeJSPlugin
import net.impleri.itemskills.integrations.kubejs.events.EventsBinding
import net.impleri.itemskills.integrations.kubejs.events.RestrictionsRegistrationEventJS
import net.minecraft.server.MinecraftServer

class ItemSkillsPlugin : KubeJSPlugin() {
  init {
    LifecycleEvent.SERVER_STARTING.register(
      LifecycleEvent.ServerState { onStartup(it) },
    )
  }

  override fun registerEvents() {
    EventsBinding.GROUP.register()
  }

  companion object {
    fun onStartup(server: MinecraftServer) {
      EventsBinding.RESTRICTIONS.post(RestrictionsRegistrationEventJS(server))
    }
  }
}
