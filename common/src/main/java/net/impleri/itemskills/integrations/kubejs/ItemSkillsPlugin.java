package net.impleri.itemskills.integrations.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.impleri.itemskills.integrations.kubejs.events.EventsBinding;
import net.impleri.itemskills.integrations.kubejs.events.ItemSkillEventsJS;
import net.impleri.itemskills.integrations.kubejs.events.RestrictionJS;
import net.impleri.playerskills.integration.kubejs.events.SkillsEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class ItemSkillsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        EventsBinding.GROUP.register();
    }

    public static void loadRestrictions() {
        EventsBinding.RESTRICTIONS.post(new ItemSkillEventsJS());
    }
}
