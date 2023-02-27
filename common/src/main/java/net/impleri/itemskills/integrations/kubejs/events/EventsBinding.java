package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public abstract class EventsBinding {
    public static final EventGroup GROUP = EventGroup.of("ItemSkillEvents");

    public static final EventHandler RESTRICTIONS = GROUP.server("register", () -> RestrictionsRegistrationEventJS.class);
}
