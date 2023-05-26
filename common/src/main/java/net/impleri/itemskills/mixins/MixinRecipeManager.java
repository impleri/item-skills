package net.impleri.itemskills.mixins;

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
import java.util.List;
import java.util.Optional;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
    private boolean isProducible(Recipe<?> recipe) {
        var item = recipe.getResultItem().getItem();

        var hasUncomsumable = recipe.getIngredients().stream()
                .map(Ingredient::getItems)
                .flatMap(Arrays::stream)
                .anyMatch(stack -> !ClientApi.INSTANCE.isConsumable(stack.getItem()));

        return ClientApi.INSTANCE.isProducible(item) && !hasUncomsumable;
    }

    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
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
    public <C extends Container, T extends Recipe<C>> void onGetRecipesFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<List<T>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        cir.setReturnValue(
                value.stream()
                        .filter(this::isProducible)
                        .toList()
        );

    }
}
