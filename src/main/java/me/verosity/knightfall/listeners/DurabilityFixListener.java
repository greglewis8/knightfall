package me.verosity.knightfall.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.net.http.WebSocket;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DurabilityFixListener implements Listener {

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        Material type = item.getType();

        // Check if the item is armor (e.g., helmet, chestplate, leggings, or boots)
        if (isArmor(type)) {
            int originalDurabilityLoss = event.getDamage();
            // Reduce the durability loss by half.
            // (Using integer division here means it will round down; you can adjust as needed.)
            int reducedDurabilityLoss = originalDurabilityLoss / 2;

            event.setDamage(reducedDurabilityLoss);
        }
    }

    // Helper method to determine if a Material is an armor piece
    private boolean isArmor(Material type) {
        String name = type.toString();
        return name.endsWith("_HELMET") ||
                name.endsWith("_CHESTPLATE") ||
                name.endsWith("_LEGGINGS") ||
                name.endsWith("_BOOTS");
    }
}

