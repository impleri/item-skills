package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.playerskills.utils.RegistrationType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    private final MinecraftServer server;

    public RestrictionsRegistrationEventJS(MinecraftServer server) {
        this.server = server;
    }

    public void restrict(String itemName, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        RegistrationType<Item> registrationType = new RegistrationType<Item>(itemName, net.minecraft.core.Registry.ITEM_REGISTRY);

        registrationType.ifNamespace(namespace -> restrictNamespace(namespace, consumer));
        registrationType.ifName(name -> restrictItem(name, consumer));
        registrationType.ifTag(tag -> restrictTag(tag, consumer));
    }

    @HideFromJS
    private void restrictItem(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name, server);

        consumer.accept(builder);

        var item = ItemHelper.getItem(name);

        if (ItemHelper.isEmptyItem(item)) {
            ConsoleJS.SERVER.warn("Could not find any item named " + name.toString());
        }

        var restriction = builder.createObject(item);
        ConsoleJS.SERVER.info("Created item restriction for " + item);
        ItemSkills.RESTRICTIONS.add(name, restriction);
    }

    @HideFromJS
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating item restrictions for mod namespace " + namespace);
        net.minecraft.core.Registry.ITEM.keySet()
                .stream()
                .filter(itemName -> itemName.getNamespace().equals(namespace))
                .forEach(itemName -> restrictItem(itemName, consumer));
    }

    @HideFromJS
    private void restrictTag(TagKey<Item> tag, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        ConsoleJS.SERVER.info("Creating item restrictions for tag " + tag);
        net.minecraft.core.Registry.ITEM.stream()
                .filter(item -> item.getDefaultInstance().is(tag))
                .forEach(item -> restrictItem(ItemHelper.getItemKey(item), consumer));
    }
}
