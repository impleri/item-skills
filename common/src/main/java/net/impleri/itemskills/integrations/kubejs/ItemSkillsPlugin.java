package net.impleri.itemskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.itemskills.integrations.kubejs.events.EventsBinding;
import net.impleri.itemskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;

public class ItemSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup() {
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS());
    }
}
