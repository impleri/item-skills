package net.impleri.itemskills.integrations.trinkets;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.impleri.itemskills.InventoryHelper;
import net.impleri.itemskills.ItemHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class TrinketsSkills {
    public static void onServerTick(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(TrinketsSkills::onPlayerTick);

    }

    private static void onPlayerTick(Player player) {
        TrinketsApi.getTrinketComponent(player).ifPresent(handleTrinket(player));
    }

    private static Consumer<TrinketComponent> handleTrinket(Player player) {
        return (component) -> {
            component.getAllEquipped().forEach(tuple -> {
                var stack = tuple.getB();
                if (!ItemHelper.isWearable(player, stack.getItem(), null)) {
                    var slot = tuple.getA();
                    slot.inventory().setItem(slot.index(), ItemStack.EMPTY);
                    InventoryHelper.moveItemIntoInventory(player, stack);
                }
            });
        };
    }
}
