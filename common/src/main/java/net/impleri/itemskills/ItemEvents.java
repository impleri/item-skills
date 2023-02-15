package net.impleri.itemskills;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.value.IntValue;
import net.impleri.itemskills.api.Restrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

class ItemEvents {
    public void registerEventHandlers() {
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
    }

    private EventResult beforePlayerPickup(Player player, ItemEntity entity, ItemStack stack) {
        var item = ItemHelper.getItemKey(stack);
        ItemSkills.LOGGER.debug("{} is about to pickup {}", player.getName().getString(), item);

        if (Restrictions.INSTANCE.isHoldable(player, item)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private void onPlayerTick(Player player) {
        if (player.getLevel().isClientSide) {
            return;
        }

        Inventory inventory = player.getInventory();

        // Move unwearable items from armor and offhand into normal inventory
        ItemHelper.isListWearable(player, inventory.armor);
        ItemHelper.isListWearable(player, inventory.offhand);
        ItemHelper.isListHoldable(player, inventory.offhand);

        // Drop unholdable items from inventory
        ArrayList<ItemStack> itemsToRemove = new ArrayList<>();
        inventory.items.forEach(stack -> {
            var item = ItemHelper.getItemKey(stack);
            if (item.equals(ItemHelper.defaultItem)) {
                return;
            }

            if (!Restrictions.INSTANCE.isHoldable(player, item)) {
                ItemSkills.LOGGER.debug("{} should not be holding {}", player.getName().getString(), item);
                itemsToRemove.add(stack);
            }
        });

        itemsToRemove.forEach(stack -> {
            ItemSkills.LOGGER.debug("Removing {} from {}'s inventory", ItemHelper.getItemKey(stack), player.getName().getString());
            inventory.removeItem(stack);
            player.drop(stack, true);
        });
    }

    private EventResult beforePlayerAttack(LivingEntity entity, DamageSource source, float amount) {
        var attacker = source.getEntity();
        if (attacker instanceof Player player) {
            var weapon = ItemHelper.getItemKey(player.getMainHandItem());

            ItemSkills.LOGGER.debug("{} is about to attack {} using {} for {} damage", player.getName().getString(), entity.getName().getString(), weapon, amount);

            if (Restrictions.INSTANCE.isHarmful(player, weapon)) {
                return EventResult.pass();
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }

    private EventResult beforeMine(Level level, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp) {
        var tool = ItemHelper.getItemKey(player.getMainHandItem());

        ItemSkills.LOGGER.debug("{} is about to mine {} using {}", player.getName().getString(), tool, state.getBlock().getName().getString());

        if (Restrictions.INSTANCE.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private EventResult beforeInteractEntity(Player player, Entity entity, InteractionHand hand) {
        var itemUsed = ItemHelper.getItemUsed(player, hand);
        var tool = ItemHelper.getItemKey(itemUsed);

        ItemSkills.LOGGER.debug("{} is about to interact with entity {} using {}", player.getName().getString(), entity.getName().getString(), tool);

        if (Restrictions.INSTANCE.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }

    private CompoundEventResult<ItemStack> beforeUseItem(Player player, InteractionHand hand) {
        var itemUsed = ItemHelper.getItemUsed(player, hand);
        var tool = ItemHelper.getItemKey(itemUsed);

        ItemSkills.LOGGER.debug("{} is about to use {}", player.getName().getString(), tool);

        if (Restrictions.INSTANCE.isUsable(player, tool)) {
            return CompoundEventResult.pass();
        }

        return CompoundEventResult.interruptFalse(null);
    }

    private EventResult beforeUseItemBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        var itemUsed = ItemHelper.getItemUsed(player, hand);
        var tool = ItemHelper.getItemKey(itemUsed);

        ItemSkills.LOGGER.debug("{} is about to interact with block {} using {}", player.getName().getString(), player.level.getBlockState(pos).getBlock().getName().getString(), tool);

        if (Restrictions.INSTANCE.isUsable(player, tool)) {
            return EventResult.pass();
        }

        return EventResult.interruptFalse();
    }
}
