package me.verosity.knightfall.commands;

import me.verosity.knightfall.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class KingdomCommand implements CommandExecutor {

    private final Scoreboard scoreboard;

    public KingdomCommand() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if arguments exist
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String teamName = args[1];

            // Check if team already exists
            if (scoreboard.getTeam(teamName) != null) {
                player.sendMessage(ChatColor.RED + "A kingdom with that name already exists.");
                return true;
            }

            // Create a new team
            Kingdom kingdom = new Kingdom(teamName, player, true);


        }else {
            player.sendMessage(ChatColor.RED + "Usage: /kingdom create <teamname>");
        }



        return true;
    }
}
