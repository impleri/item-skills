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
    private final Map<Item, Boolean> producibility = new HashMap<>();
    private final Map<Item, Boolean> consumability = new HashMap<>();

    public SkillsDisplayVisibility() {
        ClientSkillsUpdatedEvent.EVENT.register(this::clearCache);
    }

    private void clearCache(ClientSkillsUpdatedEvent clientSkillsUpdatedEvent) {
        producibility.clear();
        consumability.clear();
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
        return !producibility.computeIfAbsent(item, ClientApi.INSTANCE::isProducible);
    }

    private boolean isntConsumable(Item item) {
        return !consumability.computeIfAbsent(item, ClientApi.INSTANCE::isConsumable);
    }

    private boolean isFilteredAs(Item item, Predicate<Item> predicate) {
        return predicate.test(item);
    }

    private boolean isFilteredAs(ItemStack stack, Predicate<Item> predicate) {
        return !stack.isEmpty() && predicate.test(stack.getItem());
    }

    private boolean hasHiddenOutput(EntryStack<?> entry) {
        if (entry.isEmpty()) {
            return false;
        }

        var value = entry.getValue();

        if (value instanceof Item item) {
            return isFilteredAs(item, this::isntProducible);
        }

        if (value instanceof ItemStack stack) {
            return isFilteredAs(stack, this::isntProducible);
        }

        return false;
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
            return isFilteredAs(item, this::isntConsumable);
        }

        if (value instanceof ItemStack stack) {
            return isFilteredAs(stack, this::isntConsumable);
        }

        return false;
    }
}
