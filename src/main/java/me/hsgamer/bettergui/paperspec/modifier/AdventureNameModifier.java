package me.hsgamer.bettergui.paperspec.modifier;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaComparator;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaModifier;
import me.hsgamer.hscore.common.StringReplacer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class AdventureNameModifier implements ItemMetaModifier, ItemMetaComparator {
    private String name; // Stored as MiniMessage representation

    @Override
    public @NotNull ItemMeta modifyMeta(ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        Component displayName = AdventureUtils.toComponent(uuid, stringReplacer.replaceOrOriginal(name, uuid));
        Component noItalic = AdventureUtils.disableItalic(displayName);
        meta.displayName(noItalic);
        return meta;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean loadFromItemMeta(ItemMeta meta) {
        if (!meta.hasDisplayName()) {
            return false;
        }
        this.name = AdventureUtils.toMiniMessage(meta.displayName());
        return true;
    }

    @Override
    public boolean compare(ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        String replaced = stringReplacer.replaceOrOriginal(name, uuid);
        // Since text components are complex, we compare the plain text representation for equality
        Component displayName = meta.displayName();
        Component compareName = AdventureUtils.toComponent(uuid, replaced);
        return Objects.equals(displayName, compareName);
    }

    @Override
    public Object toObject() {
        return this.name;
    }

    @Override
    public void loadFromObject(Object object) {
        this.name = String.valueOf(object);
    }
}
