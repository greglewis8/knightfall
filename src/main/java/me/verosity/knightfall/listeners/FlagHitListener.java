package me.verosity.knightfall.listeners;

import me.verosity.knightfall.Flag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

public class FlagHitListener implements Listener {

    @EventHandler
    public void onFlagHit(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Zombie) {
            Zombie hitZombie = (Zombie) entity;

            if (me.verosity.knightfall.Flag.isFlag(hitZombie)) {
                if (e.getDamager() instanceof Player) {
                    Player player = (Player) e.getDamager();

                    Flag.damageFlag(hitZombie);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Flag HP: " + ChatColor.WHITE + (int) Flag.getFlagHP(hitZombie) + "❤"));
                }
            }
        }
    }
}
