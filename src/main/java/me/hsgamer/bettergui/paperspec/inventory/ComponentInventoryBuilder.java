package me.hsgamer.bettergui.paperspec.inventory;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIDisplay;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIHolder;
import me.hsgamer.hscore.bukkit.gui.BukkitGUIUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.function.BiFunction;

public class ComponentInventoryBuilder implements BiFunction<BukkitGUIDisplay, UUID, Inventory> {

    @Override
    public Inventory apply(BukkitGUIDisplay display, UUID uuid) {
        BukkitGUIHolder holder = display.getHolder();
        InventoryType type = holder.getInventoryType();
        int size = holder.getSize(uuid);

        // Adventure Text Component API will throw a runtime exception
        // if the content string contains any legacy Mojang color codes.
        // So we strip it in case the menu fails to be loaded.
        // See: https://github.com/KyoriPowered/adventure/blob/529d407d55ca50e6dee10f5696063f97e34ae9b5/api/src/main/java/net/kyori/adventure/text/TextComponentImpl.java#L40
        String title = ChatColor.stripColor(holder.getTitle(uuid));

        Component adventureTitle = AdventureUtils.toComponent(uuid, title);
        return type == InventoryType.CHEST && size > 0
                ? Bukkit.createInventory(display, BukkitGUIUtils.normalizeToChestSize(size), adventureTitle)
                : Bukkit.createInventory(display, type, adventureTitle);
    }
}
