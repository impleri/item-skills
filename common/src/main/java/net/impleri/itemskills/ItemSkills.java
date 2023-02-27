package net.impleri.itemskills;

import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class ItemSkills {
    public static final String MOD_ID = "itemskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "ITEMS");

    private static final ItemEvents INSTANCE = new ItemEvents();

    public static void init() {
        LOGGER.info("Loaded Item Skills");
        INSTANCE.registerEventHandlers();
    }
}
