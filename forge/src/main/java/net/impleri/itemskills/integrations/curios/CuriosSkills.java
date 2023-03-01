package net.impleri.itemskills.integrations.curios;

import net.impleri.itemskills.InventoryHelper;
import net.impleri.itemskills.ItemHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber()
public class CuriosSkills {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && ModList.get().isLoaded("curios")) {
            CuriosApi.getCuriosHelper().getEquippedCurios(event.player).ifPresent(handleCurios(event.player));
        }
    }

    private static NonNullConsumer<IItemHandlerModifiable> handleCurios(Player player) {
        return (IItemHandlerModifiable curios) -> {
            var slotCount = curios.getSlots();

            if (slotCount > 0) {
                for (int idx = 0; idx <= slotCount; idx++) {
                    var slot = curios.getStackInSlot(idx);

                    if (!ItemHelper.isWearable(player, slot.getItem())) {
                        var badItem = curios.extractItem(idx, slot.getCount(), false);

                        InventoryHelper.moveItemIntoInventory(player, badItem);
                    }
                }
            }
        };
    }
}
