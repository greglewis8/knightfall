package me.verosity.knightfall;

import me.verosity.knightfall.commands.FlagRemoveCommand;
import me.verosity.knightfall.commands.GiveFlagCommand;
import me.verosity.knightfall.commands.KingdomCommand;
import me.verosity.knightfall.listeners.DurabilityFixListener;
import me.verosity.knightfall.listeners.FlagHitListener;
import me.verosity.knightfall.listeners.PlaceFlagItemListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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

        getServer().getPluginManager().registerEvents(new FlagHitListener(), this);
        getServer().getPluginManager().registerEvents(new Flag(), this);
        getServer().getPluginManager().registerEvents(new DurabilityFixListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceFlagItemListener(), this);

        getCommand("flagremove").setExecutor(new FlagRemoveCommand());
        getCommand("kingdom").setExecutor(new KingdomCommand());
        getCommand("giveflag").setExecutor(new GiveFlagCommand());


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
