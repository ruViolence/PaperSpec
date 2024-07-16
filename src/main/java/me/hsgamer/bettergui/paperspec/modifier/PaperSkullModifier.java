package me.hsgamer.bettergui.paperspec.modifier;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaComparator;
import me.hsgamer.hscore.bukkit.item.modifier.ItemMetaModifier;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MINUTES;

public class PaperSkullModifier implements ItemMetaModifier, ItemMetaComparator {
    private static final LoadingCache<String, PlayerProfile> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, MINUTES)
            .initialCapacity(10)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull PlayerProfile load(@NotNull String key) {
                    PlayerProfile playerProfile;
                    Optional<UUID> optionalUUID = Validate.getUUID(key);
                    if (optionalUUID.isPresent()) {
                        UUID uuid = optionalUUID.get();
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            playerProfile = player.getPlayerProfile();
                        } else {
                            playerProfile = Bukkit.createProfile(uuid);
                        }
                    } else {
                        Player player = Bukkit.getPlayer(key);
                        if (player != null) {
                            playerProfile = player.getPlayerProfile();
                        } else {
                            playerProfile = Bukkit.createProfile(key);
                        }
                    }
                    return playerProfile;
                }
            });
    private String headValue = "Steve";

    @Override
    public @NotNull ItemMeta modifyMeta(@NotNull ItemMeta itemMeta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return itemMeta;
        }
        String value = stringReplacer.replaceOrOriginal(headValue, uuid);
        try {
            PlayerProfile playerProfile = CACHE.get(value);
            skullMeta.setPlayerProfile(playerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemMeta;
    }

    @Override
    public boolean loadFromItemMeta(ItemMeta itemMeta) {
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return false;
        }
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile != null) {
            if (playerProfile.getId() != null) {
                headValue = playerProfile.getId().toString();
            } else {
                headValue = playerProfile.getName();
            }
        }
        return true;
    }

    @Override
    public boolean compare(@NotNull ItemMeta itemMeta, UUID uuid, @NotNull StringReplacer stringReplacer) {
        if (!(itemMeta instanceof SkullMeta skullMeta)) {
            return false;
        }
        String value = stringReplacer.replaceOrOriginal(headValue, uuid);
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile == null) {
            return false;
        }
        try {
            PlayerProfile playerProfile1 = CACHE.get(value);
            return playerProfile1.equals(playerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object toObject() {
        return headValue;
    }

    @Override
    public void loadFromObject(Object o) {
        headValue = String.valueOf(o);
    }
}
