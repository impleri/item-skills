package net.impleri.itemskills.mixins;

import net.impleri.itemskills.ItemHelper;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;

@Mixin(CraftingMenu.class)
public class MixinCraftingMenu {

    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/Container;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private static <C extends Container, T extends Recipe<C>> void onGetRecipeFor(AbstractContainerMenu abstractContainerMenu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer, CallbackInfo ci) {
        var server = level.getServer();
        if (server == null) {
            return;
        }

        Optional<CraftingRecipe> value = server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
        if (value.isEmpty()) {
            return;
        }

        var hasUncomsumable = value.get().getIngredients().stream()
                .map(Ingredient::getItems)
                .flatMap(Arrays::stream)
                .anyMatch(stack -> !ItemHelper.isConsumable(player, stack.getItem(), player.getOnPos()));

        if (hasUncomsumable || !ItemHelper.isProducible(player, value.get(), player.getOnPos())) {
            ci.cancel();
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(abstractContainerMenu.containerId, abstractContainerMenu.incrementStateId(), 0, ItemStack.EMPTY));
        }
    }
}
