package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String itemName, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        if (itemName.trim().endsWith(":*")) {
            var namespace = itemName.substring(0, itemName.indexOf(":"));

            restrictNamespace(namespace, consumer);
            return;
        }

        var name = SkillResourceLocation.of(itemName);
        restrictItem(name, consumer);
    }

    @HideFromJS
    private void restrictItem(ResourceLocation name, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var item = ItemHelper.getItem(name);

        if (ItemHelper.isEmptyItem(item)) {
            ConsoleJS.SERVER.warn("Could not find any item named " + name.toString());
        }

        var restriction = builder.createObject(item);
        ConsoleJS.SERVER.info("Created item restriction for " + item);
        Registry.INSTANCE.add(name, restriction);
    }

    @HideFromJS
    private void restrictNamespace(String namespace, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        net.minecraft.core.Registry.ITEM.keySet()
                .stream()
                .filter(itemName -> itemName.getNamespace().equals(namespace))
                .forEach(itemName -> restrictItem(itemName, consumer));
    }
}
