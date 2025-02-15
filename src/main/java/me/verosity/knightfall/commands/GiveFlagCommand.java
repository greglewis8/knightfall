package me.verosity.knightfall.commands;

import me.verosity.knightfall.items.FlagItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveFlagCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            System.out.println("Only a player can execute this command");
            return false;
        }
        Player player = (Player) sender;

        if (!player.isOp()){
            System.out.println("Only an operator can use this command");
            return false;
        }

        ItemStack flag = new FlagItem().getItem();
        player.getInventory().addItem(flag);
        player.sendMessage("i gave u a flag gerbert");

        return true;
    }
}
