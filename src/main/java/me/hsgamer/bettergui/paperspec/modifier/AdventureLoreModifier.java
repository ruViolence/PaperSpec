package me.hsgamer.bettergui.paperspec.modifier;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaComparator;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.StringReplacer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdventureLoreModifier implements ItemMetaModifier, ItemMetaComparator {
    private final List<String> lore = new ArrayList<>(); // Stored as MiniMessage representation

    private List<String> getReplacedLore(UUID uuid, StringReplacer stringReplacer) {
        List<String> replacedLore = new ArrayList<>(lore);
        replacedLore.replaceAll(s -> stringReplacer.replaceOrOriginal(s, uuid));
        return replacedLore;
    }

    @Override
    public @NotNull ItemMeta modifyMeta(ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        List<Component> lore = AdventureUtils.toComponent(uuid, getReplacedLore(uuid, stringReplacer));
        List<Component> noItalic = AdventureUtils.disableItalic(lore);
        meta.lore(noItalic);
        return meta;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean loadFromItemMeta(ItemMeta meta) {
        if (!meta.hasLore()) {
            return false;
        }
        lore.clear();
        lore.addAll(AdventureUtils.toMiniMessage(meta.lore()));
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean compare(ItemMeta meta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        if (!meta.hasLore() && this.lore.isEmpty()) {
            return true;
        }

        // Since text components are complex, we compare the plain text representation for equality
        List<Component> itemLore = meta.lore();
        List<Component> compareLore = AdventureUtils.toComponent(uuid, getReplacedLore(uuid, stringReplacer));
        if (itemLore.size() != compareLore.size()) {
            return false;
        }
        for (int i = 0; i < itemLore.size(); i++) {
            if (!itemLore.get(i).equals(compareLore.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object toObject() {
        return lore;
    }

    @Override
    public void loadFromObject(Object object) {
        lore.clear();
        lore.addAll(CollectionUtils.createStringListFromObject(object, false));
    }
}
