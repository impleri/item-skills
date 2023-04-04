package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.restrictions.Restriction;
import net.impleri.playerskills.restrictions.AbstractRegistrationEventJS;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RestrictionsRegistrationEventJS extends AbstractRegistrationEventJS<Item, Restriction, RestrictionJS.Builder> {
    public RestrictionsRegistrationEventJS(MinecraftServer s) {
        super(s, "item", Registry.ITEM);
    }

    @HideFromJS
    protected void restrictOne(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name, server);

        consumer.accept(builder);

        var item = ItemHelper.getItem(name);

        if (ItemHelper.isEmptyItem(item)) {
            ConsoleJS.SERVER.warn("Could not find any item named " + name.toString());
        }

        var restriction = builder.createObject(item);
        ItemSkills.RESTRICTIONS.add(name, restriction);

        logRestrictionCreation(restriction, name);
    }

    @Override
    public Predicate<Item> isTagged(TagKey<Item> tag) {
        return item -> item.getDefaultInstance().is(tag);
    }

    @Override
    public ResourceLocation getName(Item resource) {
        return ItemHelper.getItemKey(resource);
    }
}
