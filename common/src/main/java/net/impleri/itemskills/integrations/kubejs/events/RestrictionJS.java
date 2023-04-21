package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.integration.kubejs.api.AbstractRestrictionBuilder;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;

public class RestrictionJS extends Restriction {
    private static final ResourceKey<Registry<Restriction>> key = ResourceKey.createRegistryKey(SkillResourceLocation.of("item_restriction_builders_registry"));

    public static final RegistryObjectBuilderTypes<Restriction> registry = RegistryObjectBuilderTypes.add(key, Restriction.class);

    public RestrictionJS(Item item, Builder builder) {
        super(
                item,
                builder.condition,
                builder.replacement,
                builder.includeDimensions,
                builder.excludeDimensions,
                builder.includeBiomes,
                builder.excludeBiomes,
                builder.producible,
                builder.consumable,
                builder.holdable,
                builder.identifiable,
                builder.harmful,
                builder.wearable,
                builder.usable
        );
    }

    public static class Builder extends AbstractRestrictionBuilder<Restriction> {
        public Item replacement;

        public boolean producible = true;
        public boolean consumable = true;
        public boolean holdable = true;
        public boolean identifiable = true;
        public boolean harmful = true;
        public boolean wearable = true;
        public boolean usable = true;

        @HideFromJS
        public Builder(ResourceLocation id, MinecraftServer server) {
            super(id, server);
        }

        public Builder replaceWith(ResourceLocation replacement) {
            this.replacement = ItemHelper.getItem(replacement);

            return this;
        }

        public Builder producible() {
            this.producible = true;
            this.holdable = true;

            return this;
        }

        public Builder unproducible() {
            this.producible = false;

            return this;
        }

        public Builder consumable() {
            this.consumable = true;
            this.holdable = true;

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
            this.producible = false;
            this.consumable = false;
            this.harmful = false;
            this.wearable = false;
            this.usable = false;

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
            this.holdable = true;

            return this;
        }

        public Builder harmless() {
            this.harmful = false;

            return this;
        }

        public Builder wearable() {
            this.wearable = true;
            this.holdable = true;

            return this;
        }

        public Builder unwearable() {
            this.wearable = false;

            return this;
        }

        public Builder usable() {
            this.usable = true;
            this.holdable = true;

            return this;
        }

        public Builder unusable() {
            this.usable = false;

            return this;
        }

        public Builder nothing() {
            this.producible = true;
            this.consumable = true;
            this.holdable = true;
            this.identifiable = true;
            this.harmful = true;
            this.wearable = true;
            this.usable = true;

            return this;
        }

        public Builder everything() {
            this.producible = false;
            this.consumable = false;
            this.holdable = false;
            this.identifiable = false;
            this.harmful = false;
            this.wearable = false;
            this.usable = false;

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
            return null;
        }

        @HideFromJS
        public Restriction createObject(Item item) {
            return new RestrictionJS(item, this);
        }
    }
}
