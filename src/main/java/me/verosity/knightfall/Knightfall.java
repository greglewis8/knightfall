package me.verosity.knightfall;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Knightfall extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Knightfall has been enabled.");

        getServer().getPluginManager().registerEvents(new XPBottleBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ShearSheepListener(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Knightfall has been disabled.");


    }

// REGISTER LISTENERS IF WANNA USE
//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent event){
//        System.out.println("A player has joined the server.");
//    }
//
//    @EventHandler
//    public void onLeaveBed(PlayerBedLeaveEvent event){
//        Player player = event.getPlayer();
//        player.sendMessage("STOP GOONING! DIE 4 GOON");
//        player.setHealth(0.0);
//    }


}
