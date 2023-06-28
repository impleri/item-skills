package net.impleri.itemskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventHandler

object EventsBinding {
  val GROUP: EventGroup = EventGroup.of("ItemSkillEvents")
  val RESTRICTIONS: EventHandler = GROUP.server("register") { RestrictionsRegistrationEventJS::class.java }
}
