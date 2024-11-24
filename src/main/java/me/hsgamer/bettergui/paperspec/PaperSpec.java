package me.hsgamer.bettergui.paperspec;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.InventoryBuilder;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.paperspec.action.ComponentBroadcastAction;
import me.hsgamer.bettergui.paperspec.action.ComponentTellAction;
import me.hsgamer.bettergui.paperspec.inventory.ComponentInventoryBuilder;
import me.hsgamer.bettergui.paperspec.modifier.AdventureLoreModifier;
import me.hsgamer.bettergui.paperspec.modifier.AdventureNameModifier;
import me.hsgamer.bettergui.paperspec.modifier.PaperSkullModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.expansion.common.Expansion;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.NotNull;

public final class PaperSpec implements Expansion {
    private static @NotNull BukkitAudiences audiences;

    public static BukkitAudiences getAudiences() {
        return audiences;
    }

    @Override
    public boolean onLoad() {
        try {
            Class.forName("net.kyori.adventure.text.Component");
        } catch (ClassNotFoundException e) {
            return false;
        }

        return Validate.isClassLoaded("com.destroystokyo.paper.profile.PlayerProfile")
                && Validate.isClassLoaded("net.kyori.adventure.text.minimessage.MiniMessage")
                && Validate.isClassLoaded("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer");
    }

    @Override
    public void onEnable() {
        audiences = BukkitAudiences.builder(BetterGUI.getInstance()).build();
        
        ItemModifierBuilder.INSTANCE.register(PaperSkullModifier::new, "paper-skull", "paper-head", "skull$", "head$");
        ItemModifierBuilder.INSTANCE.register(AdventureNameModifier::new, "mini-name", "name$");
        ItemModifierBuilder.INSTANCE.register(AdventureLoreModifier::new, "mini-lore", "lore$");

        InventoryBuilder.INSTANCE.register(pair -> new ComponentInventoryBuilder(pair.getValue()), "mini-title");

        ActionBuilder.INSTANCE.register(ComponentBroadcastAction::new, "component-broadcast", "paper-broadcast", "adventure-broadcast", "broadcast$");
        ActionBuilder.INSTANCE.register(ComponentTellAction::new, "component-tell", "paper-tell", "adventure-tell", "tell$");
    }
}
