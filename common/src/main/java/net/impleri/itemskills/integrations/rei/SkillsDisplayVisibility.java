package net.impleri.itemskills.integrations.rei;

import dev.architectury.event.EventResult;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.impleri.itemskills.client.ClientApi;

import java.util.List;
import java.util.function.Predicate;

public class SkillsDisplayVisibility implements DisplayVisibilityPredicate {
    @Override
    public double getPriority() {
        return 100.0;
    }

    @Override
    public EventResult handleDisplay(DisplayCategory<?> category, Display display) {
        if (matchAnyIngredientInList(display.getOutputEntries(), this::hasHiddenOutput)) {
            return EventResult.interruptFalse();
        }

        if (matchAnyIngredientInList(display.getInputEntries(), this::hasHiddenInput)) {
            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    private boolean matchAnyIngredientInList(List<EntryIngredient> entries, Predicate<EntryStack<?>> predicate) {
        return entries.stream()
                .anyMatch(entry -> entry.stream()
                        .anyMatch(predicate)
                );
    }

    /**
     * Checks every ingredient to see if any are uncraftable
     */
    private boolean hasHiddenOutput(EntryStack<?> entry) {
        return !ClientApi.isProducible(entry.getIdentifier());
    }

    /**
     * Checks every ingredient to see if any are supposed to be hidden
     */
    private boolean hasHiddenInput(EntryStack<?> entry) {
        return !ClientApi.isConsumable(entry.getIdentifier());
    }
}
