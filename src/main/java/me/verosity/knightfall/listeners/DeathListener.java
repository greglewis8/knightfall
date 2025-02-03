package me.verosity.knightfall.listeners;

import me.verosity.knightfall.Knightfall;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

// Constructor Injection
public class DeathListener implements Listener {

    private final Knightfall instanceAccess;

    public DeathListener(Knightfall instanceAccess){
        this.instanceAccess = instanceAccess;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
    }
}
