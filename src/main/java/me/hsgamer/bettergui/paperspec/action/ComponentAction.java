package me.hsgamer.bettergui.paperspec.action;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class ComponentAction implements Action {
    protected final String value;
    protected final List<String> options;
    private final BiFunction<UUID, String, Component> serializer;
    private final boolean trimColor;

    protected ComponentAction(ActionBuilder.Input input) {
        this.value = input.getValue();
        options = input.getOptionAsList();
        String type = options.isEmpty() ? "legacy" : options.get(0).toLowerCase();
        if (type.contains("mini")) {
            serializer = AdventureUtils::toComponent;
            trimColor = true;
        } else if (type.equalsIgnoreCase("json") || type.equalsIgnoreCase("gson")) {
            serializer = (uuid, s) -> GsonComponentSerializer.colorDownsamplingGson().deserialize(s);
            trimColor = false;
        } else {
            serializer = (uuid, s) -> LegacyComponentSerializer.legacyAmpersand().deserialize(s);
            trimColor = false;
        }
    }

    protected abstract void accept(Player player, Component component);

    @Override
    public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }

        String replaced = stringReplacer.replaceOrOriginal(value, uuid);
        if (trimColor) {
            replaced = ChatColor.stripColor(replaced);
        }
        Component component = serializer.apply(uuid, replaced);

        SchedulerUtil.global().run(() -> {
            accept(player, component);
            process.next();
        });
    }
}
