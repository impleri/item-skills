package net.impleri.itemskills.mixins;

import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.api.Restrictions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "setItemSlot", at = @At(value = "HEAD"), cancellable = true)
    public void onEquip(EquipmentSlot equipmentSlot, ItemStack itemStack, CallbackInfo ci) {
        if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
            if (!Restrictions.isWearable(ItemSkills.getItemKey(itemStack))) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "addItem", at = @At(value = "HEAD"), cancellable = true)
    public void onAddItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (!Restrictions.isHoldable(ItemSkills.getItemKey(itemStack))) {
            cir.setReturnValue(false);
        }
    }
}
