package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.impleri.itemskills.integrations.kubejs.PlayerSkillDataJS;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class RestrictionJS extends Restriction {
    private static final ResourceKey<Registry<Restriction>> key = ResourceKey.createRegistryKey(SkillResourceLocation.of("item_restriction_builders_registry"));

    public static final RegistryObjectBuilderTypes<Restriction> registry = RegistryObjectBuilderTypes.add(key, Restriction.class);

    public RestrictionJS(Builder builder) {
        super(
                builder.id,
                builder.condition,
                builder.producible,
                builder.consumable,
                builder.holdable,
                builder.identifiable,
                builder.harmful,
                builder.wearable,
                builder.usable
        );
    }

    public static class Builder extends BuilderBase<Restriction> {
        @HideFromJS
        public Predicate<Player> condition = (Player player) -> true;

        public boolean producible = true;
        public boolean consumable = true;
        public boolean holdable = true;
        public boolean identifiable = true;
        public boolean harmful = true;
        public boolean wearable = true;
        public boolean usable = true;

        @HideFromJS
        public Builder(ResourceLocation id) {
            super(id);
        }


        @RemapForJS("if")
        public Builder condition(Predicate<PlayerSkillDataJS> consumer) {
            this.condition = (Player player) -> consumer.test(new PlayerSkillDataJS(player));

            return this;
        }

        public Builder unless(Predicate<PlayerSkillDataJS> consumer) {
            this.condition = (Player player) -> !consumer.test(new PlayerSkillDataJS(player));

            return this;
        }

        public Builder producible() {
            this.producible = true;
            holdable = true;

            return this;
        }

        public Builder unproducible() {
            this.producible = false;

            return this;
        }

        public Builder consumable() {
            this.consumable = true;
            holdable = true;

            return this;
        }

        public Builder unconsumable() {
            this.consumable = false;

            return this;
        }

        public Builder holdable() {
            this.holdable = true;

            return this;
        }

        public Builder unholdable() {
            this.holdable = false;
            producible = false;
            consumable = false;
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
            producible = true;
            consumable = true;
            holdable = true;
            identifiable = true;
            harmful = true;
            wearable = true;
            usable = true;

            return this;
        }

        public Builder everything() {
            producible = false;
            consumable = false;
            holdable = false;
            identifiable = false;
            harmful = false;
            wearable = false;
            usable = false;

            return this;
        }

        @HideFromJS
        @Override
        public RegistryObjectBuilderTypes<Restriction> getRegistryType() {
            return registry;
        }

        @HideFromJS
        @Override
        public Restriction createObject() {
            return new RestrictionJS(this);
        }
    }
}
