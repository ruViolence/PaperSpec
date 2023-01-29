package me.hsgamer.bettergui.paperspec.action;

import me.hsgamer.bettergui.builder.ActionBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ComponentBroadcastBarAction extends ComponentAction {
    public ComponentBroadcastBarAction(ActionBuilder.Input input) {
        super(input);
    }

    @Override
    protected void accept(Player player, Component component) {
        player.getServer().sendActionBar(component);
    }
}
