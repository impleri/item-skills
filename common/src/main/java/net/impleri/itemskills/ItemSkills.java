package net.impleri.itemskills;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.value.IntValue;
import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.integrations.kubejs.ItemSkillsPlugin;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.PlayerSkillsLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        PlayerEvent.PICKUP_ITEM_PRE.register(this::beforePlayerPickup);
        EntityEvent.LIVING_HURT.register(this::beforePlayerAttack);
        BlockEvent.BREAK.register(this::beforeMine);
        InteractionEvent.LEFT_CLICK_BLOCK.register(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(this::beforeUseItemBlock);
        InteractionEvent.INTERACT_ENTITY.register(this::beforeInteractEntity);
        InteractionEvent.RIGHT_CLICK_ITEM.register(this::beforeUseItem);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    private void unregisterEventHandlers() {
        PlayerEvent.PICKUP_ITEM_PRE.unregister(this::beforePlayerPickup);
        EntityEvent.LIVING_HURT.unregister(this::beforePlayerAttack);
        BlockEvent.BREAK.unregister(this::beforeMine);
        InteractionEvent.LEFT_CLICK_BLOCK.unregister(this::beforeUseItemBlock);
        InteractionEvent.RIGHT_CLICK_BLOCK.unregister(this::beforeUseItemBlock);
        InteractionEvent.INTERACT_ENTITY.unregister(this::beforeInteractEntity);
        InteractionEvent.RIGHT_CLICK_ITEM.unregister(this::beforeUseItem);
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

    private EventResult beforePlayerPickup(Player player, ItemEntity entity, ItemStack stack) {
        if (Restrictions.isHoldable(player, getItemKey(stack))) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private EventResult beforePlayerAttack(LivingEntity entity, DamageSource source, float amount) {
        var attacker = source.getEntity();
        if (attacker instanceof Player player) {
            var weapon = getItemKey(player.getMainHandItem());

            LOGGER.info("Player {} is attacking {} with {} for {} damage", player.getName().getString(), entity.getName().getString(), weapon, amount);

            if (Restrictions.isHarmful(player, weapon)) {
                return EventResult.pass();
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    private EventResult beforeMine(Level level, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp) {
        var tool = getItemKey(player.getMainHandItem());

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private EventResult beforeInteractEntity(Player player, Entity entity, InteractionHand hand) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private CompoundEventResult<ItemStack> beforeUseItem(Player player, InteractionHand hand) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        if (Restrictions.isUsable(player, tool)) {
            return CompoundEventResult.pass();
        }

        return CompoundEventResult.interruptFalse(null);
    }

    private EventResult beforeUseItemBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        var itemUsed = (hand == InteractionHand.OFF_HAND) ? player.getOffhandItem() : player.getMainHandItem();
        var tool = getItemKey(itemUsed);

        if (Restrictions.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }
}
