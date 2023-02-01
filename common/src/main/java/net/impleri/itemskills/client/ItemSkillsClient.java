package net.impleri.itemskills.client;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.impleri.itemskills.ItemSkills;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemSkillsClient {
    private static final ItemSkillsClient INSTANCE = new ItemSkillsClient();

    public static void init() {
        ItemSkills.LOGGER.info("Loaded Item Skills Client");
        INSTANCE.registerEventHandlers();
    }

    private void registerEventHandlers() {
        // Tooltip
        ClientTooltipEvent.ITEM.register(this::beforeRenderItemTooltip);
    }

    private void beforeRenderItemTooltip(ItemStack stack, List<Component> lines, TooltipFlag flag) {
        var item = ItemSkills.getItemKey(stack);
        if (!ClientApi.INSTANCE.isIdentifiable(item)) {
            ItemSkills.LOGGER.debug("Replacing tooltip for {}", item);
            lines.clear();
            lines.add(Component.translatable("message.itemskills.unknown_item").withStyle(ChatFormatting.RED));
        }
    }
}
