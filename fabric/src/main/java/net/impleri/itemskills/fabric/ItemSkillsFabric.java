package net.impleri.itemskills.fabric;

import net.fabricmc.api.ModInitializer;
import net.impleri.itemskills.ItemSkills;

public class ItemSkillsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ItemSkills.init();
    }
}
