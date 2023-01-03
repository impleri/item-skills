package net.impleri.itemskills.mixins;

import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.api.Restrictions;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class MixinCraftingMenu {
    private static boolean isCraftable(Player player, Recipe<?> recipe) {
        var item = recipe.getResultItem().getItem();
        ItemSkills.LOGGER.info("Checking if {} is craftable", item);
        return Restrictions.isProducible(player, ItemSkills.getItemKey(item));
    }

    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CraftingRecipe;assemble(Lnet/minecraft/world/Container;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private static <C extends Container, T extends Recipe<C>> void onGetRecipeFor(AbstractContainerMenu abstractContainerMenu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer, CallbackInfo ci) {
        Optional<CraftingRecipe> value = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
        if (value.isEmpty()) {
            return;
        }

        if (!isCraftable(player, value.get())) {
            ci.cancel();
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(abstractContainerMenu.containerId, abstractContainerMenu.incrementStateId(), 0, ItemStack.EMPTY));
        }
    }
}
