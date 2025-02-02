package me.verosity.knightfall;

import me.verosity.knightfall.commands.FeedCommand;
import me.verosity.knightfall.commands.GodCommand;
import me.verosity.knightfall.listeners.ShearSheepListener;
import me.verosity.knightfall.listeners.XPBottleBreakListener;
import me.verosity.knightfall.listeners.tutorial.JoinLeaveListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLOutput;

public final class Knightfall extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Knightfall has been enabled.");

        getServer().getPluginManager().registerEvents(new XPBottleBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ShearSheepListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        getCommand("god").setExecutor(new GodCommand());
        getCommand("feed").setExecutor(new FeedCommand());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Knightfall has been disabled.");


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("die")){
            if(sender instanceof Player){
                Player p = (Player) sender;
                p.setHealth(0.0);
                p.sendMessage(ChatColor.RED + "You will die <3");
            }
        }else if(sender instanceof ConsoleCommandSender){
            System.out.println("The command was run by the console, u cant rlly die tbh");
        }

        return true;
    }


}
