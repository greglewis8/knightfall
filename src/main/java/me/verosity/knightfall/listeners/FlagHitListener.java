package me.verosity.knightfall.listeners;

import me.verosity.knightfall.Flag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class FlagHitListener implements Listener {

    @EventHandler
    public void onFlagHit(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Zombie) {
            Zombie hitZombie = (Zombie) entity;

            if (me.verosity.knightfall.Flag.isFlag(hitZombie)) {
                if (e.getDamager() instanceof Player) {

                    Player player = (Player) e.getDamager();
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    if (heldItem.getType() == Material.NETHERITE_INGOT) {
                        Flag.healFlag(hitZombie, 10.0);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                    } else if(heldItem.getType() == Material.DIAMOND_BLOCK) {
                        Flag.healFlag(hitZombie, 1.0);
                        heldItem.setAmount(heldItem.getAmount() - 1);
                    } else if(isCriticalHit(player)) {
                        Flag.damageFlag(hitZombie);

                    }
                }
            }
        }
    }

    private boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0.0F
                && !player.isOnGround()
                && !player.isSprinting()
                && player.getPotionEffect(PotionEffectType.BLINDNESS) == null;
    }

}
