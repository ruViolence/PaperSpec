package me.hsgamer.bettergui.paperspec.modifier;

import me.hsgamer.bettergui.paperspec.util.AdventureUtils;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdventureLoreModifier extends ItemMetaModifier {
    private final List<String> lore = new ArrayList<>(); // Stored as MiniMessage representation

    @Override
    public String getName() {
        return "adventure-lore";
    }

    private List<String> getReplacedLore(UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        List<String> replacedLore = new ArrayList<>(lore);
        replacedLore.replaceAll(s -> StringReplacer.replace(s, uuid, stringReplacerMap.values()));
        return replacedLore;
    }

    @Override
    public ItemMeta modifyMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        List<Component> lore = AdventureUtils.toComponent(uuid, getReplacedLore(uuid, stringReplacerMap));
        List<Component> noItalic = AdventureUtils.disableItalic(lore);
        meta.lore(noItalic);
        return meta;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void loadFromItemMeta(ItemMeta meta) {
        lore.clear();
        lore.addAll(AdventureUtils.toMiniMessage(meta.lore()));
    }

    @Override
    public boolean canLoadFromItemMeta(ItemMeta meta) {
        return meta.hasLore();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean compareWithItemMeta(ItemMeta meta, UUID uuid, Map<String, StringReplacer> stringReplacerMap) {
        if (!meta.hasLore() && this.lore.isEmpty()) {
            return true;
        }

        // Since text components are complex, we compare the plain text representation for equality
        List<Component> itemLore = meta.lore();
        List<Component> compareLore = AdventureUtils.toComponent(uuid, getReplacedLore(uuid, stringReplacerMap));
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
