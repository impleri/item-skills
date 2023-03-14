package net.impleri.itemskills.restrictions;

import net.impleri.playerskills.restrictions.AbstractRestriction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

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
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable,
            @Nullable Boolean identifiable,
            @Nullable Boolean harmful,
            @Nullable Boolean wearable,
            @Nullable Boolean usable
    ) {
        super(item, condition, replacement);
        this.producible = Boolean.TRUE.equals(producible);
        this.consumable = Boolean.TRUE.equals(consumable);
        this.holdable = Boolean.TRUE.equals(holdable);
        this.identifiable = Boolean.TRUE.equals(identifiable);
        this.harmful = Boolean.TRUE.equals(harmful);
        this.wearable = Boolean.TRUE.equals(wearable);
        this.usable = Boolean.TRUE.equals(usable);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable,
            @Nullable Boolean identifiable,
            @Nullable Boolean harmful,
            @Nullable Boolean wearable
    ) {
        this(item, condition, replacement, producible, consumable, holdable, identifiable, harmful, wearable, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable,
            @Nullable Boolean identifiable,
            @Nullable Boolean harmful
    ) {
        this(item, condition, replacement, producible, consumable, holdable, identifiable, harmful, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable,
            @Nullable Boolean identifiable
    ) {
        this(item, condition, replacement, producible, consumable, holdable, identifiable, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible,
            @Nullable Boolean consumable,
            @Nullable Boolean holdable
    ) {
        this(item, condition, replacement, producible, consumable, holdable, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible,
            @Nullable Boolean consumable
    ) {
        this(item, condition, replacement, producible, consumable, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement,
            @Nullable Boolean producible
    ) {
        this(item, condition, replacement, producible, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition,
            @Nullable Item replacement
    ) {
        this(item, condition, replacement, null);
    }

    public Restriction(
            Item item,
            Predicate<Player> condition
    ) {
        this(item, condition, null);
    }
}
