package net.impleri.itemskills.mixins;

import com.mojang.datafixers.util.Pair;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.api.Restrictions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

// Always client-side

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    private boolean isCraftable(Recipe<?> recipe) {
        var item = recipe.getResultItem().getItem();
        var location = Registry.ITEM.getKey(item);
        return Restrictions.isCraftable(location);
    }

    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
    public <C extends Container, T extends Recipe<C>> void onGetRecipeFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<Optional<T>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        if (!isCraftable(value.get())) {
            ItemSkills.LOGGER.info("Marking recipe as uncraftable");
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
    public <C extends Container, T extends Recipe<C>> void onGetSpecificRecipeFor(RecipeType<T> recipeType, C container, Level level, ResourceLocation resourceLocation, CallbackInfoReturnable<Optional<Pair<ResourceLocation, T>>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        if (!isCraftable(value.get().getSecond())) {
            ItemSkills.LOGGER.info("Marking recipe as uncraftable");
            cir.setReturnValue(Optional.empty());
        }
    }

    @Inject(method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At(value = "RETURN"), cancellable = true)
    public <C extends Container, T extends Recipe<C>> void onGetRecipesFor(RecipeType<T> recipeType, C container, Level level, CallbackInfoReturnable<Optional<T>> cir) {
        var value = cir.getReturnValue();
        if (value.isEmpty()) {
            return;
        }

        if (!isCraftable(value.get())) {
            ItemSkills.LOGGER.info("Marking recipe as uncraftable");
            cir.setReturnValue(Optional.empty());
        }
    }
}
