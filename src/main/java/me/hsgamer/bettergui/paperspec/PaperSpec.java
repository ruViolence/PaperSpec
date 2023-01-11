package me.hsgamer.bettergui.paperspec;

import me.hsgamer.bettergui.builder.InventoryBuilder;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.paperspec.inventory.ComponentInventoryBuilder;
import me.hsgamer.bettergui.paperspec.modifier.AdventureLoreModifier;
import me.hsgamer.bettergui.paperspec.modifier.AdventureNameModifier;
import me.hsgamer.bettergui.paperspec.modifier.PaperSkullModifier;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public final class PaperSpec extends PluginAddon {
    @Override
    public boolean onLoad() {
        Class<?> componentClass;
        try {
            componentClass = Class.forName("net.kyori.adventure.text.Component");
        } catch (ClassNotFoundException e) {
            return false;
        }

        return Validate.isClassLoaded("com.destroystokyo.paper.profile.PlayerProfile")
                && Validate.isClassLoaded("net.kyori.adventure.text.minimessage.MiniMessage")
                && Validate.isClassLoaded("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer")
                && Validate.isMethodLoaded("org.bukkit.inventory.meta.ItemMeta", "lore", List.class)
                && Validate.isMethodLoaded("org.bukkit.inventory.meta.ItemMeta", "lore")
                && Validate.isMethodLoaded("org.bukkit.inventory.meta.ItemMeta", "displayName", componentClass)
                && Validate.isMethodLoaded("org.bukkit.inventory.meta.ItemMeta", "displayName")
                && Validate.isMethodLoaded("org.bukkit.Bukkit", "createInventory", InventoryHolder.class, int.class, componentClass);
    }

    @Override
    public void onEnable() {
        ItemModifierBuilder.INSTANCE.register(PaperSkullModifier::new, "paper-skull", "paper-head", "skull$", "head$");
        ItemModifierBuilder.INSTANCE.register(AdventureNameModifier::new, "mini-name", "name$");
        ItemModifierBuilder.INSTANCE.register(AdventureLoreModifier::new, "mini-lore", "lore$");

        InventoryBuilder.INSTANCE.register(ComponentInventoryBuilder::new, "mini-title");
    }
}
