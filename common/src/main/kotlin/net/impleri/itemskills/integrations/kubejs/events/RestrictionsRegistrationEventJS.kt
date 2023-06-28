package net.impleri.itemskills.integrations.kubejs.events

import dev.latvian.mods.kubejs.server.ServerEventJS
import net.impleri.itemskills.ItemSkills
import net.impleri.itemskills.api.RestrictionBuilder
import net.minecraft.server.MinecraftServer

class RestrictionsRegistrationEventJS(s: MinecraftServer) : ServerEventJS(s) {
  fun restrict(name: String, consumer: (RestrictionConditionsBuilderJS) -> Unit) {
    val builder = RestrictionConditionsBuilderJS(ItemSkills.server)
    consumer(builder)

    RestrictionBuilder.register(name, builder)
  }
}
