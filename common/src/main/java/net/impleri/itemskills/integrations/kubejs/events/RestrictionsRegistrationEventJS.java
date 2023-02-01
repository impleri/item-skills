package net.impleri.itemskills.integrations.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.impleri.itemskills.restrictions.Registry;
import net.impleri.playerskills.utils.SkillResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RestrictionsRegistrationEventJS extends EventJS {
    public void restrict(String item, @NotNull Consumer<RestrictionJS.Builder> consumer) {
        var name = SkillResourceLocation.of(item);
        var builder = new RestrictionJS.Builder(name);

        consumer.accept(builder);

        var restriction = builder.createObject();
        ConsoleJS.STARTUP.info("Created item restriction for " + item);
        Registry.INSTANCE.add(name, restriction);
    }
}
