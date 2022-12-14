package net.impleri.itemskills.forge;

import dev.architectury.platform.forge.EventBuses;
import net.impleri.itemskills.ItemSkills;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ItemSkills.MOD_ID)
public class ItemSkillsForge {
    public ItemSkillsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ItemSkills.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        ItemSkills.init();
    }
}
