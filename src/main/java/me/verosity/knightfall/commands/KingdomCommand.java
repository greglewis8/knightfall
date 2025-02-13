package me.verosity.knightfall.commands;

import me.verosity.knightfall.ColorConverter;
import me.verosity.knightfall.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KingdomCommand implements CommandExecutor {

    // Store invitations with their expiration times
    private final Map<UUID, Invitation> invitations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // /kingdom create <name>
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            createKingdom(args, player);
        }
        // /kingdom color <color>
        else if (args.length == 2 && args[0].equalsIgnoreCase("color")) {
            changeKingdomColor(args, player);
        }
        // /kingdom invite <player>
        else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
            invitePlayer(args, player);
        }
        // /kingdom accept <kingdom name>
        else if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            acceptInvitation(args[1], player);
        }


        return true;
    }



    private void invitePlayer(String[] args, Player sender) {
        // Check if the sender is a kingdom leader
        if (!Kingdom.isKing(sender)) {
            sender.sendMessage(ChatColor.RED + "You must be a kingdom leader to invite players.");
            return;
        }

        String playerName = args[1];
        Player invitee = Bukkit.getPlayer(playerName);

        // Check if the player is online
        if (invitee == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        // Check if the sender and invitee are in the same kingdom
        Kingdom senderKingdom = Kingdom.findLeaderKingdom(sender);
        if (senderKingdom.getKingdomPlayers().contains(invitee)) {
            sender.sendMessage(ChatColor.RED + "That player is already in your kingdom.");
            return;
        }

        // Create and store the invitation with expiration time (5 minutes)
        Invitation invitation = new Invitation(sender.getName(), senderKingdom.getKingdomName(), System.currentTimeMillis() + (5 * 60 * 1000));
        invitations.put(invitee.getUniqueId(), invitation);

        invitee.sendMessage(ChatColor.GREEN + sender.getName() + " has invited you to join their kingdom: " + senderKingdom.getKingdomName() + ". Type '/kingdom accept " + senderKingdom.getKingdomName() + "' to join.");
    }

    private void acceptInvitation(String kingdomName, Player player) {
        // Check if the player has a valid invitation
        Invitation invitation = invitations.get(player.getUniqueId());

        if (invitation == null) {
            player.sendMessage(ChatColor.RED + "You don't have any kingdom invitations.");
            return;
        }

        // Check if the invitation has expired
        if (System.currentTimeMillis() > invitation.getExpirationTime()) {
            invitations.remove(player.getUniqueId()); // Remove expired invitation
            player.sendMessage(ChatColor.RED + "Your invitation has expired.");
            return;
        }

        // Check if the kingdom name matches the one in the invitation
        if (!kingdomName.equalsIgnoreCase(invitation.getKingdomName())) {
            player.sendMessage(ChatColor.RED + "The kingdom name does not match the invitation.");
            return;
        }

        // Get the inviter's kingdom and add the player to it
        Player inviter = Bukkit.getPlayer(invitation.getInviterName());
        if (inviter == null) {
            player.sendMessage(ChatColor.RED + "The player who invited you is no longer online.");
            return;
        }

        Kingdom inviterKingdom = Kingdom.findLeaderKingdom(inviter);
        //inviterKingdom.getKingdomPlayers().add(player);

        inviterKingdom.addNewMember(player);

        // Inform the player and inviter
        player.sendMessage(ChatColor.GREEN + "You have successfully joined the kingdom " + inviterKingdom.getKingdomName() + "!");
        inviter.sendMessage(ChatColor.GREEN + player.getName() + " has joined your kingdom!");

        // Remove the invitation after accepting
        invitations.remove(player.getUniqueId());
    }

    private void changeKingdomColor(String[] args, Player player) {
        String colorString = args[1];
        if (Kingdom.isKing(player)) {
            Kingdom playerKingdom = Kingdom.findLeaderKingdom(player);
            if (ColorConverter.VALID_COLORS.contains(colorString)) {
                Kingdom.changeColor(colorString, playerKingdom);
            }
        }
    }

    private void createKingdom(String[] args, Player player) {
        String teamName = args[1];

        // Check if the kingdom already exists
        if (Kingdom.getKingdoms().stream().anyMatch(k -> k.getKingdomName().equalsIgnoreCase(teamName))) {
            player.sendMessage(ChatColor.RED + "A kingdom with that name already exists.");
            return;
        }

        // Create a new kingdom
        new Kingdom(teamName, player, true);
        player.sendMessage(ChatColor.GREEN + "Your kingdom " + teamName + " has been created!");
    }

    // Inner class to store invitation data
    private static class Invitation {
        private final String inviterName;
        private final String kingdomName;
        private final long expirationTime;

        public Invitation(String inviterName, String kingdomName, long expirationTime) {
            this.inviterName = inviterName;
            this.kingdomName = kingdomName;
            this.expirationTime = expirationTime;
        }

        public String getInviterName() {
            return inviterName;
        }

        public String getKingdomName() {
            return kingdomName;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }
}
