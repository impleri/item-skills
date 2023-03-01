package net.impleri.itemskills.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.impleri.itemskills.ItemSkills;

public class ItemSkillsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemSkills.init();
        registerTrinkets();
    }

    private void registerTrinkets() {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            ServerTickEvents.START_SERVER_TICK.register(net.impleri.itemskills.integrations.trinkets.TrinketsSkills::onServerTick);
        }
    }
}
