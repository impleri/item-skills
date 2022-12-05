package net.impleri.itemskills;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import net.impleri.itemskills.api.Restrictions;
import net.impleri.itemskills.integrations.kubejs.ItemSkillsPlugin;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.PlayerSkillsLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        PlayerEvent.PICKUP_ITEM_PRE.register(this::beforePlayerPickup);
        EntityEvent.LIVING_HURT.register(this::beforePlayerAttack);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, this);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        Registry.clear();
        ItemSkillsPlugin.loadRestrictions();
    }

    private ResourceLocation getItemKey(ItemStack stack) {
        return getItemKey(stack.getItem());
    }

    private ResourceLocation getItemKey(Item item) {
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
}
