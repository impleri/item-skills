package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.integrations.kubejs.PlayerSkillDataJS;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.SkillResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;
import java.util.function.Predicate;

public class RestrictionJS extends Restriction {
    private static final ResourceKey<Registry<Restriction>> key = ResourceKey.createRegistryKey(SkillResourceLocation.of("item_restriction_builders_registry"));

    public static final RegistryObjectBuilderTypes<Restriction> registry = RegistryObjectBuilderTypes.add(key, Restriction.class);

    public RestrictionJS(Builder builder) {
        super(
                builder.id,
                builder.condition,
                builder.craftable,
                builder.visible,
                builder.holdable,
                builder.identifiable,
                builder.harmful,
                builder.wearable,
                builder.usable
        );
    }

    public static class Builder extends BuilderBase<Restriction> {
        public Function<Player, Boolean> condition;
        public boolean craftable = false;
        public boolean visible = false;
        public boolean holdable = false;
        public boolean identifiable = false;
        public boolean harmful = false;
        public boolean wearable = false;
        public boolean usable = false;

        public Builder(ResourceLocation id) {
            super(id);
        }

        @RemapForJS("if")
        public Builder condition(Function<PlayerSkillDataJS, Boolean> consumer) {
            this.condition = (Player player) -> consumer.apply(new PlayerSkillDataJS(player));

            return this;
        }

        public Builder unless(Function<PlayerSkillDataJS, Boolean> consumer) {
            this.condition = (Player player) -> !consumer.apply(new PlayerSkillDataJS(player));

            return this;
        }

        public Builder craftable() {
            this.craftable = true;
            holdable = true;

            return this;
        }

        public Builder uncraftable() {
            this.craftable = false;

            return this;
        }

        public Builder visible() {
            this.visible = true;
            holdable = true;

            return this;
        }

        public Builder hidden() {
            this.visible = false;

            return this;
        }

        public Builder holdable() {
            this.holdable = true;

            return this;
        }

        public Builder unholdable() {
            this.holdable = false;
            craftable = false;
            visible = false;
            harmful = false;
            wearable = false;
            usable = false;

            return this;
        }

        public Builder identifiable() {
            this.identifiable = true;

            return this;
        }

        public Builder unidentifiable() {
            this.identifiable = false;

            return this;
        }

        public Builder harmful() {
            this.harmful = true;
            holdable = true;

            return this;
        }

        public Builder harmless() {
            this.harmful = false;

            return this;
        }

        public Builder wearable() {
            this.wearable = true;
            holdable = true;

            return this;
        }

        public Builder unwearable() {
            this.wearable = false;

            return this;
        }

        public Builder usable() {
            this.usable = true;
            holdable = true;

            return this;
        }

        public Builder unusable() {
            this.usable = false;

            return this;
        }

        public Builder nothing() {
            craftable = true;
            visible = true;
            holdable = true;
            identifiable = true;
            harmful = true;
            wearable = true;
            usable = true;

            return this;
        }

        public Builder everything() {
            craftable = false;
            visible = false;
            holdable = false;
            identifiable = false;
            harmful = false;
            wearable = false;
            usable = false;

            return this;
        }

        @Override
        public RegistryObjectBuilderTypes<Restriction> getRegistryType() {
            return registry;
        }

        @Override
        public Restriction createObject() {
            return new RestrictionJS(this);
        }
    }
}
