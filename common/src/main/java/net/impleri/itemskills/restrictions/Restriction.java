package net.impleri.itemskills.restrictions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Restriction {
    public final ResourceLocation item;
    public final Function<Player, Boolean> condition;
    public final boolean craftable;
    public final boolean visible;
    public final boolean holdable;
    public final boolean identifiable;
    public final boolean harmful;
    public final boolean wearable;
    public final boolean usable;

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible,
        @Nullable Boolean holdable,
        @Nullable Boolean identifiable,
        @Nullable Boolean harmful,
        @Nullable Boolean wearable,
        @Nullable Boolean usable
    ) {
        this.item = item;
        this.condition = condition;
        this.craftable = Boolean.TRUE.equals(craftable);
        this.visible = Boolean.TRUE.equals(visible);
        this.holdable = Boolean.TRUE.equals(holdable);
        this.identifiable = Boolean.TRUE.equals(identifiable);
        this.harmful = Boolean.TRUE.equals(harmful);
        this.wearable = Boolean.TRUE.equals(wearable);
        this.usable = Boolean.TRUE.equals(usable);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible,
        @Nullable Boolean holdable,
        @Nullable Boolean identifiable,
        @Nullable Boolean harmful,
        @Nullable Boolean wearable
    ) {
        this(item, condition, craftable, visible, holdable, identifiable, harmful, wearable, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible,
        @Nullable Boolean holdable,
        @Nullable Boolean identifiable,
        @Nullable Boolean harmful
    ) {
        this(item, condition, craftable, visible, holdable, identifiable, harmful, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible,
        @Nullable Boolean holdable,
        @Nullable Boolean identifiable
    ) {
        this(item, condition, craftable, visible, holdable, identifiable, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible,
        @Nullable Boolean holdable
    ) {
        this(item, condition, craftable, visible, holdable, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable,
        @Nullable Boolean visible
    ) {
        this(item, condition, craftable, visible, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition,
        @Nullable Boolean craftable
    ) {
        this(item, condition, craftable, null);
    }

    public Restriction(
        ResourceLocation item,
        Function<Player, Boolean> condition
    ) {
        this(item, condition, null);
    }
}
