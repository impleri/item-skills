package net.impleri.itemskills.mixins;

import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.client.ClientApi;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
    private boolean isProducible(Recipe<?> recipe) {
        var item = recipe.getResultItem().getItem();
        ItemSkills.LOGGER.info("Checking if {} is producible", item);

        var hasUncomsumable = recipe.getIngredients().stream()
                .map(Ingredient::getItems)
                .flatMap(Arrays::stream)
                .anyMatch(stack -> !ClientApi.INSTANCE.isConsumable(stack.getItem()));

        return ClientApi.INSTANCE.isProducible(item) && !hasUncomsumable;
    }

    @Inject(method = "getRecipeFor", at = @At(value = "RETURN"), cancellable = true)
    public <C extends Container, T extends Recipe<C>> void onGetRecipeFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<Optional<T>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        if (!isProducible(value.get())) {
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "getRecipesFor", at = @At(value = "RETURN"), cancellable = true)
    public <C extends Container, T extends Recipe<C>> void onGetRecipesFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<Optional<T>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        if (!isProducible(value.get())) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
