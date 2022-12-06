package net.impleri.itemskills;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.*;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.value.IntValue;
import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.integrations.kubejs.ItemSkillsPlugin;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.PlayerSkillsLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSkills implements ResourceManagerReloadListener {
    public static final String MOD_ID = "itemskills";
    public static final PlayerSkillsLogger LOGGER = PlayerSkillsLogger.create(MOD_ID, "PS-ITEM");

    private static final ItemSkills eventHandler = new ItemSkills();

    public static void init() {
        LOGGER.info("Loaded Item Skills");
        eventHandler.registerEventHandlers();
    }

    public static void end() {
        eventHandler.unregisterEventHandlers();
    }

    private void registerEventHandlers() {
        // holdable
        PlayerEvent.PICKUP_ITEM_PRE.register(this::beforePlayerPickup);

        // holdable AND wearable
        TickEvent.PLAYER_POST.register(this::onPlayerTick);

        // harmful
        EntityEvent.LIVING_HURT.register(this::beforePlayerAttack);

        // usable
        BlockEvent.BREAK.register(this::beforeMine);
        InteractionEvent.LEFT_CLICK_BLOCK.register(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_ITEM.register(this::beforeUseItem);
        InteractionEvent.INTERACT_ENTITY.register(this::beforeInteractEntity);

        // Tooltip
        ClientTooltipEvent.ITEM.register(this::beforeRenderItemTooltip);

        // Reload
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    private void unregisterEventHandlers() {
        // holdable
        PlayerEvent.PICKUP_ITEM_PRE.unregister(this::beforePlayerPickup);

        // holdable AND wearable
        TickEvent.PLAYER_POST.unregister(this::onPlayerTick);

        // harmful
        EntityEvent.LIVING_HURT.unregister(this::beforePlayerAttack);

        // usable
        BlockEvent.BREAK.unregister(this::beforeMine);
        InteractionEvent.LEFT_CLICK_BLOCK.unregister(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_BLOCK.unregister(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_ITEM.unregister(this::beforeUseItem);
        InteractionEvent.INTERACT_ENTITY.unregister(this::beforeInteractEntity);

        // Tooltip
        ClientTooltipEvent.ITEM.unregister(this::beforeRenderItemTooltip);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        Registry.clear();
        ItemSkillsPlugin.loadRestrictions();
    }

    public static ResourceLocation getItemKey(ItemStack stack) {
        return getItemKey(stack.isEmpty() ? null : stack.getItem());
    }

    public static ResourceLocation getItemKey(Item item) {
        return net.minecraft.core.Registry.ITEM.getKey(item);
    }

    private static final ResourceLocation defaultItem = getItemKey((Item) null);

    private EventResult beforePlayerPickup(Player player, ItemEntity entity, ItemStack stack) {
        var item = getItemKey(stack);
        LOGGER.debug("{} is about to pickup {}", player.getName().getString(), item);

        if (Restrictions.isHoldable(player, item)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private void onPlayerTick(Player player) {
        Inventory inventory = player.getInventory();
        HashMap<Integer, ItemStack> armorToRemove = new HashMap<>();
        ArrayList<ItemStack> itemsToRemove = new ArrayList<>();

        Arrays.stream(Inventory.ALL_ARMOR_SLOTS).forEach(armorSlot -> {
            var stack = inventory.getArmor(armorSlot);
            var item = getItemKey(stack);
            if (item.equals(defaultItem)) {
                return;
            }

            if (!Restrictions.isWearable(player, item)) {
                LOGGER.debug("{} should not be wearing {}", player.getName().getString(), item);
                armorToRemove.put(armorSlot, stack);
            }
        });

        armorToRemove.forEach((key, value) -> {
            LOGGER.debug("Removing {} from {}'s equipped armor", getItemKey(value), player.getName().getString());
            inventory.armor.set(key, ItemStack.EMPTY);
            inventory.placeItemBackInInventory(value);
        });

        inventory.items.forEach(stack -> {
            var item = getItemKey(stack);
            if (item.equals(defaultItem)) {
                return;
            }

            if (!Restrictions.isHoldable(player, item)) {
                LOGGER.debug("{} should not be holding {}", player.getName().getString(), item);
                itemsToRemove.add(stack);
            }
        });

        itemsToRemove.forEach(stack -> {
            LOGGER.debug("Removing {} from {}'s inventory", getItemKey(stack), player.getName().getString());
            inventory.removeItem(stack);
            player.drop(stack, true);
        });
    }

    private EventResult beforePlayerAttack(LivingEntity entity, DamageSource source, float amount) {
        var attacker = source.getEntity();
        if (attacker instanceof Player player) {
            var weapon = getItemKey(player.getMainHandItem());

            LOGGER.debug("{} is about to attack {} using {} for {} damage", player.getName().getString(), entity.getName().getString(), weapon, amount);

            if (Restrictions.isHarmful(player, weapon)) {
                return EventResult.pass();
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    private EventResult beforeMine(Level level, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp) {
        var tool = getItemKey(player.getMainHandItem());

        LOGGER.debug("{} is about to mine {} using {}", player.getName().getString(), tool, state.getBlock().getName().getString());

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private EventResult beforeInteractEntity(Player player, Entity entity, InteractionHand hand) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        LOGGER.debug("{} is about to interact with entity {} using {}", player.getName().getString(), entity.getName().getString(), tool);

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private CompoundEventResult<ItemStack> beforeUseItem(Player player, InteractionHand hand) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        LOGGER.debug("{} is about to use {}", player.getName().getString(), tool);

        if (Restrictions.isUsable(player, tool)) {
            return CompoundEventResult.pass();
        }

        return CompoundEventResult.interruptFalse(null);
    }

    private EventResult beforeUseItemBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        LOGGER.debug("{} is about to interact with block {} using {}", player.getName().getString(), player.level.getBlockState(pos).getBlock().getName().getString(), tool);

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private void beforeRenderItemTooltip(ItemStack stack, List<Component> lines, TooltipFlag flag) {
        var item = getItemKey(stack);
        if (!Restrictions.isIdentifiable(item)) {
            LOGGER.info("Replacing tooltip for {}", getItemKey(stack));
            lines.clear();
            lines.add(Component.translatable("message.itemskills.unknown_item").withStyle(ChatFormatting.RED));
        }
    }
}
