package net.impleri.itemskills.client;

import dev.architectury.event.events.client.ClientTooltipEvent;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.playerskills.client.events.ClientSkillsUpdatedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSkillsClient {
    private static final ItemSkillsClient INSTANCE = new ItemSkillsClient();

    public static void init() {
        ItemSkills.LOGGER.info("Loaded Item Skills Client");
        INSTANCE.registerEventHandlers();
    }

    private void registerEventHandlers() {
        // Tooltip
        ClientTooltipEvent.ITEM.register(this::beforeRenderItemTooltip);
        ClientSkillsUpdatedEvent.EVENT.register(this::clearCache);
    }

    private Map<Item, Boolean> identifiability = new HashMap<>();

    private void clearCache(ClientSkillsUpdatedEvent clientSkillsUpdatedEvent) {
        identifiability.clear();
    }

    private Boolean populateCache(Item item) {
        return ClientApi.INSTANCE.isIdentifiable(item);
    }

    private void beforeRenderItemTooltip(ItemStack stack, List<Component> lines, TooltipFlag flag) {
        var item = ItemHelper.getItem(stack);

        if (!identifiability.computeIfAbsent(item, this::populateCache)) {
            ItemSkills.LOGGER.debug("Replacing tooltip for {}", item);
            lines.clear();
            lines.add(Component.translatable("message.itemskills.unknown_item").withStyle(ChatFormatting.RED));
        }
    }
}
