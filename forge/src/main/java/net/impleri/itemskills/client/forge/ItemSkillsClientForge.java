package net.impleri.itemskills.client.forge;

import net.impleri.itemskills.client.ItemSkillsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ItemSkillsClientForge {
    @SubscribeEvent
    public static void onClientInit(FMLClientSetupEvent event) {
        ItemSkillsClient.init();
    }

}
