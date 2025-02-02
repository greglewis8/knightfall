package me.verosity.knightfall.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = (Player) sender;
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.GREEN + "U are now fed <3");
        } else{
            System.out.println("hey ur not a player !");
        }
        return true;
    }
}
