package me.hsgamer.bettergui.paperspec.inventory;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.gui.GUIDisplay;
import me.hsgamer.hscore.bukkit.gui.GUIHolder;
import me.hsgamer.hscore.bukkit.gui.GUIUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.function.BiFunction;

public class ComponentInventoryBuilder implements BiFunction<GUIDisplay, UUID, Inventory> {

    @Override
    public Inventory apply(GUIDisplay display, UUID uuid) {
        GUIHolder holder = display.getHolder();
        InventoryType type = holder.getInventoryType();
        int size = holder.getSize(uuid);

        // Adventure Text Component API will throw a runtime exception
        // if the content string contains any legacy Mojang color codes.
        // So we strip it in case the menu fails to be loaded.
        // See: https://github.com/KyoriPowered/adventure/blob/529d407d55ca50e6dee10f5696063f97e34ae9b5/api/src/main/java/net/kyori/adventure/text/TextComponentImpl.java#L40
        String title = ChatColor.stripColor(holder.getTitle(uuid));

        Component adventure$title = AdventureUtils.toComponent(title);
        return type == InventoryType.CHEST && size > 0
                ? Bukkit.createInventory(display, GUIUtils.normalizeToChestSize(size), adventure$title)
                : Bukkit.createInventory(display, type, adventure$title);
    }
}
