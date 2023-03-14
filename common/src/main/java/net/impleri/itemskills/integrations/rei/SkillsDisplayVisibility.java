package net.impleri.itemskills.integrations.rei;

import dev.architectury.event.EventResult;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.impleri.itemskills.client.ClientApi;
import net.impleri.playerskills.client.events.ClientSkillsUpdatedEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SkillsDisplayVisibility implements DisplayVisibilityPredicate {
    private final Map<Item, Boolean> hiddenProducibles = new HashMap<>();
    private final Map<Item, Boolean> hiddenConsumables = new HashMap<>();

    public SkillsDisplayVisibility() {
        ClientSkillsUpdatedEvent.EVENT.register(this::clearCache);
    }

    private void clearCache(ClientSkillsUpdatedEvent clientSkillsUpdatedEvent) {
        hiddenProducibles.clear();
        hiddenConsumables.clear();
    }

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
    private boolean isntProducible(Item item) {
        return !hiddenProducibles.computeIfAbsent(item, ClientApi.INSTANCE::isProducible);
    }

    private boolean hasHiddenOutput(EntryStack<?> entry) {
        if (entry.isEmpty()) {
            return false;
        }

        var value = entry.getValue();

        if (value instanceof Item item) {
            return isntProducible(item);
        }

        if (value instanceof ItemStack stack) {
            return !((ItemStack) value).isEmpty() && isntProducible(stack.getItem());
        }

        return false;
    }

    private boolean isntConsumable(Item item) {
        return !hiddenConsumables.computeIfAbsent(item, ClientApi.INSTANCE::isConsumable);
    }

    /**
     * Checks every ingredient to see if any are supposed to be hidden
     */
    private boolean hasHiddenInput(EntryStack<?> entry) {
        if (entry.isEmpty()) {
            return false;
        }

        var value = entry.getValue();

        if (value instanceof Item item) {
            return isntConsumable(item);
        }

        if (value instanceof ItemStack stack) {
            return !((ItemStack) value).isEmpty() && isntConsumable(stack.getItem());
        }

        return false;
    }
}
