package net.impleri.itemskills.integrations.kubejs;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.impleri.playerskills.client.ClientApi;
import net.impleri.playerskills.server.ServerApi;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerSkillDataJS {
    @HideFromJS
    private final Player player;

    @HideFromJS
    public PlayerSkillDataJS(Player player) {
        this.player = player;
    }

    public <T> boolean can(String skillName, @Nullable T expectedValue) {
        return (player.getLevel().isClientSide) ? ClientApi.can(skillName, expectedValue) : ServerApi.can(player, skillName, expectedValue);
    }

    public <T> boolean can(String skill) {
        return can(skill, null);
    }

    public <T> boolean cannot(String skill, @Nullable T expectedValue) {
        return !can(skill, expectedValue);
    }

    public <T> boolean cannot(String skill) {
        return cannot(skill, null);
    }
}
