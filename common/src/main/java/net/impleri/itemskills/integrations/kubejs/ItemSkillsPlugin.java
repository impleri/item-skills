package net.impleri.itemskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.itemskills.integrations.kubejs.events.EventsBinding;
import net.impleri.itemskills.integrations.kubejs.events.RestrictionsRegistrationEventJS;
import net.impleri.itemskills.restrictions.Registry;

public class ItemSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void onStartup() {
        Registry.INSTANCE.clear();
        EventsBinding.RESTRICTIONS.post(new RestrictionsRegistrationEventJS());
    }
}
