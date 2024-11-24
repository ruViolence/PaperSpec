package me.hsgamer.bettergui.paperspec.inventory;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIDisplay;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIHolder;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIUtils;
import me.hsgamer.hscore.common.MapUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class ComponentInventoryBuilder implements Function<BukkitGUIDisplay, Inventory> {
    private final String title;

    public ComponentInventoryBuilder(Map<String, Object> menuSettings) {
        this.title = MapUtils.getOptional(menuSettings, "name", "title")
                .map(Objects::toString)
                .orElse("");
    }

    @Override
    public Inventory apply(BukkitGUIDisplay display) {
        UUID uuid = display.getUniqueId();
        BukkitGUIHolder holder = display.getHolder();
        InventoryType type = holder.getInventoryType();
        int size = holder.getSize();
        Component adventureTitle = AdventureUtils.toComponent(uuid, title);
        return type == InventoryType.CHEST && size > 0
                ? Bukkit.createInventory(display, BukkitGUIUtils.normalizeToChestSize(size), LegacyComponentSerializer.legacySection().serialize(adventureTitle))
                : Bukkit.createInventory(display, type, LegacyComponentSerializer.legacySection().serialize(adventureTitle));
    }
}
