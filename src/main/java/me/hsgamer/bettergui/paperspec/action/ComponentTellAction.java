package me.hsgamer.bettergui.paperspec.action;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.paperspec.PaperSpec;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ComponentTellAction extends ComponentAction {
    private final boolean isBar;

    public ComponentTellAction(ActionBuilder.Input input) {
        super(input);
        isBar = options.size() > 1 && options.get(1).equalsIgnoreCase("bar");
    }

    @Override
    protected void accept(Player player, Component component) {
        if (isBar) {
            PaperSpec.getAudiences().player(player).sendActionBar(component);
        } else {
            PaperSpec.getAudiences().player(player).sendMessage(component);
        }
    }
}
