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

public class AnvilColorizer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
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
                    meta.setDisplayName(displayName);

                    result.setItemMeta(meta);
                    event.setResult(result);
                }
            }
        }
    }

}
