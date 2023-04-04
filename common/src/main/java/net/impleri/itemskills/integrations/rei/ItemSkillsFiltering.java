package net.impleri.itemskills.integrations.rei;

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.client.ClientApi;
import net.impleri.itemskills.utils.ListDiff;
import net.impleri.playerskills.client.events.ClientSkillsUpdatedEvent;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSkillsFiltering {
    private static BasicFilteringRule.MarkDirty filteringRule;

    private static final List<Item> currentlyFiltered = new ArrayList<>();

    public static void updateHidden(ClientSkillsUpdatedEvent event) {
        ItemSkills.LOGGER.debug("Client skills list has been updated: {}", event.next());
        var nextHidden = ClientApi.INSTANCE.getHidden();

        // Nothing on either list, so don't bother
        if (currentlyFiltered.size() == 0 && nextHidden.size() == 0) {
            ItemSkills.LOGGER.debug("No changes in restrictions");
            return;
        }

        var toHide = !ListDiff.contains(currentlyFiltered, nextHidden);
        var toReveal = !ListDiff.contains(nextHidden, currentlyFiltered);


        if ((toHide || toReveal) && filteringRule != null) {
            ItemSkills.LOGGER.debug("Updating REI filters");
            // Update what we're supposed
            currentlyFiltered.clear();
            currentlyFiltered.addAll(nextHidden);

            // Trigger re-filtering REI entry stacks
            filteringRule.markDirty();
        }

    }

    public static void register(BasicFilteringRule<?> rule) {
        filteringRule = rule.hide(() -> ClientApi.INSTANCE.getHidden().stream()
                .map(item -> EntryStacks.of(item).cast())
                .collect(Collectors.toList()));
    }
}
