package net.impleri.itemskills.integrations.curios;

import net.impleri.itemskills.InventoryHelper;
import net.impleri.itemskills.ItemHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosSkills {
    public static void onPlayerTick(Player player) {
        CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handleCurios(player));
    }

    private static NonNullConsumer<IItemHandlerModifiable> handleCurios(Player player) {
        return (IItemHandlerModifiable curios) -> {
            var slotCount = curios.getSlots();

            if (slotCount > 0) {
                for (int idx = 0; idx <= slotCount; idx++) {
                    var slot = curios.getStackInSlot(idx);

                    if (!ItemHelper.isWearable(player, slot.getItem(), null)) {
                        var badItem = curios.extractItem(idx, slot.getCount(), false);

                        InventoryHelper.moveItemIntoInventory(player, badItem);
                    }
                }
            }
        };
    }
}
