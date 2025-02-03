package me.verosity.knightfall.listeners.tutorial;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (player.hasPlayedBefore()){
            e.setJoinMessage(ChatColor.GREEN + "Welcome bakc 2 the severn " + ChatColor.BOLD + player.getDisplayName());
        }else{
            e.setJoinMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.BLUE +"Welcome 2 the severn ! Its cool pookie>");
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.RED + " has left, and gooned everywhere");
    }
}
