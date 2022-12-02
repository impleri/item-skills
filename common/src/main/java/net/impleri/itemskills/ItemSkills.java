package net.impleri.itemskills;

import dev.architectury.registry.ReloadListenerRegistry;
import net.impleri.itemskills.integrations.kubejs.ItemSkillsPlugin;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.PlayerSkillsLogger;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;

public class ItemSkills implements ResourceManagerReloadListener {
    public static final String MOD_ID = "itemskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "PS-ITEM");

    private static final ItemSkills eventHandler = new ItemSkills();

    public static void init() {
        LOGGER.info("Loaded Item Skills");
        eventHandler.registerEventHandlers();
    }

    private void registerEventHandlers() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        Registry.clear();
        ItemSkillsPlugin.loadRestrictions();
    }
}
