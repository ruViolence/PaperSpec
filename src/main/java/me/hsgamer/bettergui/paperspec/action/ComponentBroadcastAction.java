package me.hsgamer.bettergui.paperspec.action;

import me.hsgamer.bettergui.builder.ActionBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ComponentBroadcastAction extends ComponentAction {
    private final boolean isBar;

    public ComponentBroadcastAction(ActionBuilder.Input input) {
        super(input);
        isBar = options.size() > 1 && options.get(1).equalsIgnoreCase("bar");
    }

    @Override
    protected void accept(Player player, Component component) {
        if (isBar) {
            player.getServer().sendActionBar(component);
        } else {
            player.getServer().sendMessage(component);
        }
    }
}
