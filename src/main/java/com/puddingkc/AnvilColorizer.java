package com.puddingkc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnvilColorizer extends JavaPlugin implements Listener {

    private boolean supportsHexColors = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        supportsHexColors = isVersionAtLeast(1, 16);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack result = inventory.getItem(2);
        Player player = (Player) event.getViewers().get(0);

        if (player != null && player.hasPermission("anvilcolorizer.use")) {
            if (result != null && result.hasItemMeta()) {
                ItemMeta meta = result.getItemMeta();

                if (meta.hasDisplayName()) {
                    String displayName = meta.getDisplayName();
                    displayName = ChatColor.translateAlternateColorCodes('&', displayName);

                    if (supportsHexColors && player.hasPermission("anvilcolorizer.use.hex")) {
                        displayName = translateHexColorCodes(displayName);
                    }

                    meta.setDisplayName(displayName);
                    result.setItemMeta(meta);
                    event.setResult(result);
                }
            }
        }
    }

    private String translateHexColorCodes(String textToTranslate) {
        Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = hexPattern.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexColor = matcher.group();
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hexColor.substring(1).toCharArray()) {
                replacement.append('§').append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private boolean isVersionAtLeast(int major, int minor) {
        String[] versionComponents = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        int serverMajor = Integer.parseInt(versionComponents[1]);
        int serverMinor = versionComponents.length > 2 ? Integer.parseInt(versionComponents[2]) : 0;

        return (serverMajor > major) || (serverMajor == major && serverMinor >= minor);
    }
}
