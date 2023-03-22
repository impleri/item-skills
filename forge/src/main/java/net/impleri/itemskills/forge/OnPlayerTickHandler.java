package net.impleri.itemskills.forge;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class OnPlayerTickHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && ModList.get().isLoaded("curios")) {
            net.impleri.itemskills.integrations.curios.forge.CuriosSkills.onPlayerTick(event.player);
        }
    }
}
