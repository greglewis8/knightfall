package me.verosity.knightfall.listeners;

import me.verosity.knightfall.Flag;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class FlagHitListener implements Listener {

    private final HashMap<UUID, Long> hitCooldowns = new HashMap<>();
    private final long cooldownTime = 2000; // Cooldown time in milliseconds (2 seconds)

    @EventHandler
    public void onFlagHit(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Zombie) {
            Zombie hitZombie = (Zombie) entity;

            if (me.verosity.knightfall.Flag.isFlag(hitZombie)) {
                if (e.getDamager() instanceof Player) {
                    Player player = (Player) e.getDamager();
                    UUID playerId = player.getUniqueId();
                    long currentTime = System.currentTimeMillis();

                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    if (heldItem.getType() == Material.NETHERITE_INGOT) {
                        Flag.healFlag(hitZombie, 10.0);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                        return;
                    } else if (heldItem.getType() == Material.DIAMOND_BLOCK) {
                        Flag.healFlag(hitZombie, 1.0);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                        return;
                    }
                    // Check if the player is on cooldown
                    if (hitCooldowns.containsKey(playerId)) {
                        long lastHitTime = hitCooldowns.get(playerId);
                        if (currentTime - lastHitTime < cooldownTime) {
                            //player.sendMessage("§cYou must wait before hitting again!");
                            e.setCancelled(true);
                            return;
                        }
                    }

                    // Update cooldown
                    hitCooldowns.put(playerId, currentTime);
                    Flag.damageFlag(hitZombie);
                }
            }
        }
    }
}
