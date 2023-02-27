package net.impleri.itemskills.integrations.jei;

import me.shedaniel.rei.plugincompatibilities.api.REIPluginCompatIgnore;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.client.ClientApi;
import net.impleri.itemskills.utils.ListDiff;
import net.impleri.playerskills.client.events.ClientSkillsUpdatedEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@REIPluginCompatIgnore()
public class ItemSkillsJeiPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private final List<Item> currentUnconsumables = new ArrayList<>();
    private final List<Item> currentUnproducibles = new ArrayList<>();

    public ItemSkillsJeiPlugin() {
        ClientSkillsUpdatedEvent.EVENT.register(this::updateHidden);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(ItemSkills.MOD_ID, "jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;

        processUnproducibles();
    }

    private void updateHidden(ClientSkillsUpdatedEvent event) {
        if (runtime == null) {
            ItemSkills.LOGGER.warn("JE Runtime not yet available to update");
            return;
        }

        processUnconsumables();
        processUnproducibles();
    }

    private void processUnconsumables() {
        var manager = runtime.getIngredientManager();
        var next = ClientApi.INSTANCE.getUnconsumable();

        // Nothing on either list, so don't bother
        if (currentUnconsumables.size() == 0 && next.size() == 0) {
            ItemSkills.LOGGER.warn("No changes in restrictions");
            return;
        }

        ItemSkills.LOGGER.debug("Found {} unconsumable item(s)", next.size());

        var toShow = ListDiff.getMissing(currentUnconsumables, next);
        if (toShow.size() > 0) {
            ItemSkills.LOGGER.debug("Showing {} item(s) based on consumables: {}", toShow.size(), toShow.stream().map(ItemHelper::getItemKey).toList().toString());
            manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, getItemStack(toShow));
        }

        var toHide = ListDiff.getMissing(next, currentUnconsumables);
        if (toHide.size() > 0) {
            ItemSkills.LOGGER.debug("Hiding {} item(s) based on consumables: {}", toHide.size(), toHide.stream().map(ItemHelper::getItemKey).toList().toString());
            manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, getItemStack(toHide));
        }

        currentUnconsumables.clear();
        currentUnconsumables.addAll(next);
    }

    private void processUnproducibles() {
        var next = ClientApi.INSTANCE.getUnproducible();

        // Nothing on either list, so don't bother
        if (currentUnproducibles.size() == 0 && next.size() == 0) {
            ItemSkills.LOGGER.warn("No changes in restrictions");
            return;
        }

        ItemSkills.LOGGER.debug("Found {} unproducible item(s)", next.size());

        var toShow = ListDiff.getMissing(currentUnproducibles, next);

        if (toShow.size() > 0) {
            var foci = getFociFor(toShow);
            var types = getTypesFor(foci, true);

            ItemSkills.LOGGER.debug("Showing {} item(s) based on producibles: {}", toShow.size(), toShow.stream().map(ItemHelper::getItemKey).toList().toString());

            types.forEach(type -> showRecipesForType(type, foci));
        }

        var toHide = ListDiff.getMissing(next, currentUnproducibles);

        if (toHide.size() > 0) {
            var foci = getFociFor(toHide);
            var types = getTypesFor(foci, false);

            ItemSkills.LOGGER.debug("Hiding {} item(s) based on producibles: {}", toHide.size(), toHide.stream().map(ItemHelper::getItemKey).toList().toString());

            types.forEach(type -> hideRecipesForType(type, foci));
        }

        currentUnproducibles.clear();
        currentUnproducibles.addAll(next);
    }

    private List<IFocus<ItemStack>> getFociFor(List<Item> items) {
        var factory = runtime.getJeiHelpers().getFocusFactory();

        return getItemStack(items).stream()
                .map(item -> factory.createFocus(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, item))
                .toList();
    }

    private Collection<? extends RecipeType<?>> getTypesFor(List<IFocus<ItemStack>> foci, boolean includeHidden) {
        var lookup = runtime.getRecipeManager().createRecipeCategoryLookup()
                .limitFocus(foci);

        if (includeHidden) {
            lookup.includeHidden();
        }

        return lookup.get()
                .map(IRecipeCategory::getRecipeType)
                .toList();
    }

    private <T> void hideRecipesForType(RecipeType<T> type, List<IFocus<ItemStack>> foci) {
        runtime.getRecipeManager().hideRecipes(type, getRecipesFor(type, foci, false));
    }

    private <T> void showRecipesForType(RecipeType<T> type, List<IFocus<ItemStack>> foci) {
        runtime.getRecipeManager().unhideRecipes(type, getRecipesFor(type, foci, true));
    }

    private <T> Collection<T> getRecipesFor(RecipeType<T> type, List<IFocus<ItemStack>> foci, boolean includeHidden) {
        var lookup = runtime.getRecipeManager()
                .createRecipeLookup(type)
                .limitFocus(foci);

        if (includeHidden) {
            lookup.includeHidden();
        }

        return lookup.get().toList();
    }

    private Collection<ItemStack> getItemStack(List<Item> items) {
        return items.stream()
                .map(ItemStack::new)
                .toList();
    }
}
