package net.impleri.itemskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Restriction extends AbstractRestriction<Item> {
    public final boolean producible;
    public final boolean consumable;
    public final boolean holdable;
    public final boolean identifiable;
    public final boolean harmful;
    public final boolean wearable;
    public final boolean usable;

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable List<ResourceLocation> includeDimensions,
            @Nullable List<ResourceLocation> excludeDimensions,
            @Nullable List<ResourceLocation> includeBiomes,
            @Nullable List<ResourceLocation> excludeBiomes,
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable,
            @Nullable Boolean identifiable,
            @Nullable Boolean harmful,
            @Nullable Boolean wearable,
            @Nullable Boolean usable
    ) {
        super(item, condition, includeDimensions, excludeDimensions, includeBiomes, excludeBiomes, replacement);
        this.producible = Boolean.TRUE.equals(producible);
        this.consumable = Boolean.TRUE.equals(consumable);
        this.holdable = Boolean.TRUE.equals(holdable);
        this.identifiable = Boolean.TRUE.equals(identifiable);
        this.harmful = Boolean.TRUE.equals(harmful);
        this.wearable = Boolean.TRUE.equals(wearable);
        this.usable = Boolean.TRUE.equals(usable);
    }
}
