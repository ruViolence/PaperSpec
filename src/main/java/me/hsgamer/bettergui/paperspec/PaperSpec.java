package me.hsgamer.bettergui.paperspec;

import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.paperspec.modifier.PaperSkullModifier;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.common.Validate;

public final class PaperSpec extends PluginAddon {
    @Override
    public boolean onLoad() {
        return Validate.isClassLoaded("com.destroystokyo.paper.PaperConfig") || Validate.isClassLoaded("io.papermc.paper.configuration.Configuration");
    }

    @Override
    public void onEnable() {
        ItemModifierBuilder.INSTANCE.register(PaperSkullModifier::new, "paper-skull", "paper-head");
    }
}
