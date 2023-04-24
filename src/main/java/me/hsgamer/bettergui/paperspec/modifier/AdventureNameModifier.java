package me.hsgamer.bettergui.paperspec.modifier;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

public class AdventureNameModifier extends ItemMetaModifier {
    private String name; // Stored as MiniMessage representation

    @Override
    public String getName() {
        return "adventure-name";
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        Component displayName = AdventureUtils.toComponent(uuid, StringReplacer.replace(name, uuid, stringReplacerMap.values()));
        Component noItalic = AdventureUtils.disableItalic(displayName);
        meta.displayName(noItalic);
        return meta;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void loadFromItemMeta(ItemMeta meta) {
        this.name = AdventureUtils.toMiniMessage(meta.displayName());
    }

    @Override
    public boolean canLoadFromItemMeta(ItemMeta meta) {
        return meta.hasDisplayName();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        String replaced = StringReplacer.replace(this.name, uuid, stringReplacerMap.values());

        if (!meta.hasDisplayName() && replaced == null) {
            return true;
        }

        // Since text components are complex, we compare the plain text representation for equality
        Component displayName = meta.displayName();
        Component compareName = AdventureUtils.toComponent(uuid, replaced);
        return displayName.equals(compareName);
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
