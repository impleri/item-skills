package net.impleri.itemskills.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.impleri.itemskills.client.ItemSkillsClient;

public class ItemSkillsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemSkillsClient.init();
    }
}
