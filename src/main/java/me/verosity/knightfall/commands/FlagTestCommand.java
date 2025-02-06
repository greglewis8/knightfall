package me.verosity.knightfall.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;

public class FlagTestCommand implements CommandExecutor {

    private ArrayList<Zombie> flags;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(sender instanceof Player){
            Player player = (Player) sender;
            Location loc = player.getLocation();

            me.verosity.knightfall.Flag.spawnFlag(loc);
            player.sendMessage(ChatColor.GREEN + "Spawned a flag!");
        }
        return true;
    }

}
