package me.hsgamer.bettergui.paperspec.action;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.task.element.TaskProcess;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class ComponentAction extends BaseAction {
    private final ComponentSerializer<? extends Component, ? extends Component, String> serializer;
    private final boolean trimColor;

    protected ComponentAction(ActionBuilder.Input input) {
        super(input);
        if (input.option.toLowerCase().contains("mini")) {
            serializer = MiniMessage.miniMessage();
            trimColor = true;
        } else if (input.option.equalsIgnoreCase("json") || input.option.equalsIgnoreCase("gson")) {
            serializer = GsonComponentSerializer.colorDownsamplingGson();
            trimColor = false;
        } else {
            serializer = LegacyComponentSerializer.legacySection();
            trimColor = false;
        }
    }

    protected abstract void accept(Player player, Component component);

    @Override
    public void accept(UUID uuid, TaskProcess process) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }

        String replaced = getReplacedString(uuid);
        if (trimColor) {
            replaced = ChatColor.stripColor(replaced);
        }
        Component component = serializer.deserialize(replaced);

        Bukkit.getScheduler().runTask(BetterGUI.getInstance(), () -> {
            accept(player, component);
            process.next();
        });
    }
}
