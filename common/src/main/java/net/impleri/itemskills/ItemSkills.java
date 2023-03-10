package net.impleri.itemskills;

import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.Registry;
import net.impleri.playerskills.utils.PlayerSkillsLogger;

public class ItemSkills {
    public static final String MOD_ID = "itemskills";

    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "ITEMS");

    public static Registry<Restriction> RESTRICTIONS = new Registry<>(MOD_ID);

    private static final ItemEvents EVENT_HANDLER = new ItemEvents();

    public static void init() {
        LOGGER.info("Loaded Item Skills");
        EVENT_HANDLER.registerEventHandlers();
    }
}
