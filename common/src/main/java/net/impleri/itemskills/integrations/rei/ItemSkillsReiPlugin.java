package net.impleri.itemskills.integrations.rei;

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.impleri.playerskills.client.events.ClientSkillsUpdatedEvent;

public class ItemSkillsReiPlugin implements REIClientPlugin {
    public ItemSkillsReiPlugin() {
        // Subscribe to client skill updates to refresh basic filtering rules
        ClientSkillsUpdatedEvent.EVENT.register(BasicSkillsFiltering::updateHidden);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerVisibilityPredicate(new SkillsDisplayVisibility());
    }

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        BasicSkillsFiltering.register(rule);
    }
}
