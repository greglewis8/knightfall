package me.verosity.knightfall.commands;

import me.verosity.knightfall.ColorConverter;
import me.verosity.knightfall.Flag;
import me.verosity.knightfall.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

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

        // Check if args is empty or the command is incomplete
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /kingdom <command> <arguments>");
            return false; // or return true if you don't want the usage message to be shown
        }

        // /kingdom flagtph <flagIndex>
        if (args.length == 2 && args[0].equalsIgnoreCase("flagtph")) {
            teleportFlag(args, player);
        }
        // /kingdom leave
        else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
            leaveTeam(args, player);
        }
        // /kingdom disband
        else if (args.length == 1 && args[0].equalsIgnoreCase("disband")) {
            disbandTeam(args, player);
        }
        // /kingdom getflags
        else if (args.length == 1 && args[0].equalsIgnoreCase("getflags")) {
            //disbandTeam(args, player);
            getFlags(args, player);
        }
        // /kingdom create <name>
        else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
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
        // /kingdom kick <player>
        else if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
            kickPlayer(args, player);
        }

        return true;
    }

    private void getFlags (String args[], Player sender){
        if (!Kingdom.isKing(sender)) {
            sender.sendMessage(ChatColor.RED + "You must be a kingdom leader to get flags.");
            return;
        }

        Kingdom senderKingdom = Kingdom.findLeaderKingdom(sender);
        sender.sendMessage(ChatColor.YELLOW + "Possible Flags:");

        List<Zombie> kingdomFlags = senderKingdom.getKingdomFlags();
        if (kingdomFlags.isEmpty()) {
            sender.sendMessage(ChatColor.GRAY + "No flags found for your kingdom.");
        } else {
            for (int i = 0; i < kingdomFlags.size(); i++) {
                Zombie flag = kingdomFlags.get(i);
                String location = "World: " + flag.getWorld().getName() + " | XYZ: " + formatLocation(flag.getLocation());
                sender.sendMessage(ChatColor.AQUA + "Flag " + (i) + ": " + ChatColor.WHITE + location);
            }
        }
    }

    private void teleportFlag(String args[], Player sender){
        if (!Kingdom.isKing(sender)) {
            sender.sendMessage(ChatColor.RED + "You must be a kingdom leader to move a flag.");
            return;
        }

        Kingdom senderKingdom = Kingdom.findLeaderKingdom(sender);
        int flagIndex = Integer.parseInt(args[1]);
        List<Integer> flagIndexList = new ArrayList<>();

        for (int i = 0; i < senderKingdom.getKingdomFlags().size(); i++){
            flagIndexList.add(i);
        }

        if (!flagIndexList.contains(flagIndex)) {
            sender.sendMessage(ChatColor.RED + "Error: Flag does not exist!");
            getFlags(args, sender);
            return;
        }


        Zombie moveFlag = senderKingdom.getKingdomFlags().get(flagIndex);
        Location newLocation = sender.getLocation();

        Flag.flagMove(moveFlag,newLocation);


    }

    private String formatLocation(Location location) {
        return String.format("%.2f, %.2f, %.2f", location.getX(), location.getY(), location.getZ());
    }

    private void leaveTeam(String args[], Player sender) {
        if (Kingdom.isKing(sender)){
            sender.sendMessage(ChatColor.RED + "You can't leave a team if you're the leader, use /kingdom disband");
            return;
        }
        Kingdom senderKingdom = Kingdom.findPlayerKingdom(sender);

        if (senderKingdom == null){
            sender.sendMessage(ChatColor.RED + "Ur not in a kingdom bro");
            return;
        }

        senderKingdom.removePlayer(sender);
        sender.sendMessage(ChatColor.GREEN + "You left ur kingdom!");



    }

    private void disbandTeam(String args[], Player sender) {
        if (!Kingdom.isKing(sender)) {
            sender.sendMessage(ChatColor.RED + "You must be a kingdom leader to disband a team.");
            return;
        }

        Kingdom senderKingdom = Kingdom.findLeaderKingdom(sender);
        Kingdom.disbandKingdom(senderKingdom);


    }


    private void kickPlayer(String args[], Player sender){
        if (!Kingdom.isKing(sender)) {
            sender.sendMessage(ChatColor.RED + "You must be a kingdom leader to kick players.");
            return;
        }

        String playerName = args[1];
        Player target = Bukkit.getPlayer(playerName);

        // Check if target player exists and is online
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "That player is not online or does not exist.");
            return;
        }

        // Find the target player's kingdom
        Kingdom targetKingdom = Kingdom.findPlayerKingdom(target);

        // Ensure the player is in a kingdom
        if (targetKingdom == null) {
            sender.sendMessage(ChatColor.RED + "That player is not in a kingdom.");
            return;
        }

        // Ensure sender is in the same kingdom as the target
        Kingdom senderKingdom = Kingdom.findLeaderKingdom(sender);
        if (targetKingdom != senderKingdom) {
            sender.sendMessage(ChatColor.RED + "You can't kick players outside your kingdom.");
            return;
        }

        // Prevent the leader from kicking themselves
        if (Kingdom.isKing(target)) {
            sender.sendMessage(ChatColor.RED + "You can't kick yourself from your own kingdom, use /kingdom disband.");
            return;
        }

        // Remove the player from the kingdom
        targetKingdom.removePlayer(target);
        target.sendMessage(ChatColor.RED + "You have been kicked from " + targetKingdom.getKingdomName());
        sender.sendMessage(ChatColor.GREEN + "You have successfully kicked " + target.getName() + " from the kingdom.");
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
