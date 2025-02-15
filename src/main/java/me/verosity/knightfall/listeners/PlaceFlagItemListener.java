package me.verosity.knightfall.listeners;

import me.verosity.knightfall.Flag;
import me.verosity.knightfall.Kingdom;
import me.verosity.knightfall.items.FlagItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceFlagItemListener implements Listener {
    @EventHandler
    public void onPlayerPlaceFlag(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack playerMainHand = player.getInventory().getItemInMainHand();
        ItemStack flagItem = new FlagItem().getItem();

        if (!(playerMainHand.equals(flagItem))){
            return;
        }

        if(Kingdom.findLeaderKingdom(player) != null){
            player.sendMessage("flag is spanwed in :)");
            Kingdom senderKingdom = Kingdom.findLeaderKingdom(player);
            Location senderLoc = player.getLocation();

            Zombie addedFlag = Flag.spawnFlag(senderLoc, senderKingdom);
            senderKingdom.addFlag(addedFlag);
            flagItem.setAmount(flagItem.getAmount() - 1);
        }else {
            player.sendMessage("Only the leader can place donw flags");
        }
    }
}
