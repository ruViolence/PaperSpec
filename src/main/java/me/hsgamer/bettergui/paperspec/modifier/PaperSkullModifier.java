package me.hsgamer.bettergui.paperspec.modifier;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.hsgamer.hscore.bukkit.item.ItemMetaModifier;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.common.interfaces.StringReplacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MINUTES;

public class PaperSkullModifier extends ItemMetaModifier {
    private static final LoadingCache<String, PlayerProfile> CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(1, MINUTES)
            .initialCapacity(10)
            .build(new CacheLoader<String, PlayerProfile>() {
                @Override
                public PlayerProfile load(@Nonnull String key) {
                    PlayerProfile playerProfile;
                    if (Validate.isValidUUID(key)) {
                        UUID uuid = UUID.fromString(key);
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
    public ItemMeta modifyMeta(ItemMeta itemMeta, UUID uuid, Map<String, StringReplacer> map) {
        if (!(itemMeta instanceof SkullMeta)) {
            return itemMeta;
        }
        String value = StringReplacer.replace(headValue, uuid, map.values());
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        try {
            PlayerProfile playerProfile = CACHE.get(value);
            skullMeta.setPlayerProfile(playerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemMeta;
    }

    @Override
    public void loadFromItemMeta(ItemMeta itemMeta) {
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile != null) {
            if (playerProfile.getId() != null) {
                headValue = playerProfile.getId().toString();
            } else {
                headValue = playerProfile.getName();
            }
        }
    }

    @Override
    public boolean canLoadFromItemMeta(ItemMeta itemMeta) {
        return itemMeta instanceof SkullMeta;
    }

    @Override
    public boolean compareWithItemMeta(ItemMeta itemMeta, UUID uuid, Map<String, StringReplacer> map) {
        if (!(itemMeta instanceof SkullMeta)) {
            return false;
        }
        String value = StringReplacer.replace(headValue, uuid, map.values());
        SkullMeta skullMeta = (SkullMeta) itemMeta;
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
    public String getName() {
        return "paper-skull";
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
