package me.verosity.knightfall.commands;

import me.verosity.knightfall.Flag;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import java.util.List;

public class FlagRemoveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location loc = player.getLocation();
            Chunk chunk = loc.getChunk();

            Entity[] entitiesInChunk = chunk.getEntities();
            for (Entity entity : entitiesInChunk) {
                if (entity instanceof Zombie) {
                    Zombie zombie = (Zombie) entity;
                    if (Flag.isFlag(zombie)) {
                        List<Zombie> flags = Flag.getFlags();
                        List<Double> flagsHP = Flag.getFlagsHP();

                        int flagIndex = flags.indexOf(zombie);
                        if (flagIndex != -1) { // Ensure the flag exists in the list
                            flags.remove(flagIndex);
                            flagsHP.remove(flagIndex);

                            Flag.setFlags(flags);
                            Flag.setFlagsHP(flagsHP);

                            List<Entity> passengers = zombie.getPassengers();
                            for (Entity passenger : passengers) {
                                if (passenger instanceof ArmorStand) {
                                    passenger.remove();
                                }
                            }

                            zombie.remove();
                            Flag.removeBossBar(flagIndex, player);
                            player.sendMessage("Successfully removed a flag!");
                        } else {
                            player.sendMessage("Error: Could not find the flag in the list.");
                        }
                    }
                }
            }
        } else {
            sender.sendMessage("Only players can execute this command.");
        }
        return true;
    }
}
