package me.verosity.knightfall;

import me.verosity.knightfall.commands.FlagRemoveCommand;
import me.verosity.knightfall.commands.FlagTestCommand;
import me.verosity.knightfall.listeners.FlagHitListener;
import me.verosity.knightfall.listeners.JoinLeaveListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Knightfall extends JavaPlugin implements Listener {

    private static Knightfall plugin;

    public static Knightfall getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;
        // Plugin startup logic
        getLogger().info("Knightfall has been enabled.");

        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new FlagHitListener(), this);
        getServer().getPluginManager().registerEvents(new Flag(), this);

        getCommand("flagtest").setExecutor(new FlagTestCommand());
        getCommand("flagremove").setExecutor(new FlagRemoveCommand());

        File file = new File(getDataFolder(), "flags.json");
        Flag.loadFlagsFromFile(file);
        System.out.println("Flags loaded successfully.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Knightfall has been disabled.");

        File file = new File(getDataFolder(), "flags.json");
        Flag.saveFlagsToFile(file);
        System.out.println("Flags saved successfully.");

    }





}
