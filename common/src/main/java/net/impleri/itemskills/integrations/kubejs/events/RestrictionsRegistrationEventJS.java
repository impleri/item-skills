package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.impleri.itemskills.ItemHelper;
import net.impleri.itemskills.ItemSkills;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String itemName, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var name = SkillResourceLocation.of(itemName);
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var item = ItemHelper.getItem(name);

        if (ItemHelper.isEmptyItem(item)) {
            ItemSkills.LOGGER.warn("Could not find any item named {}", name);
        }

        var restriction = builder.createObject(item);
        ConsoleJS.STARTUP.info("Created item restriction for " + item);
        Registry.INSTANCE.add(name, restriction);
    }
}
