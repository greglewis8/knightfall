package me.verosity.knightfall;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Kingdom {

    private static List<Kingdom> kingdoms = new ArrayList<>();

    private String kingdomName;
    private Player kingdomLeader;
    private List<Player> kingdomPlayers;
    private String kingdomColor;
    private List<Zombie> kingdomFlags;

    public Kingdom(String name, Player leader, boolean spawnFlag) {
        kingdomName = name;
        kingdomLeader = leader;
        kingdomPlayers = new ArrayList<Player>();
        kingdomColor = "white";
        kingdomFlags = new ArrayList<Zombie>();

        kingdoms.add(this);

        if (spawnFlag) {
            Zombie flag = Flag.spawnFlag(kingdomLeader.getLocation(), this);
            kingdomFlags.add(flag);
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.registerNewTeam(kingdomName);
        team.setPrefix(ChatColor.WHITE + "[" + kingdomName + "] ");
        leader.sendMessage("Kingdom " + kingdomName + " has been created!");
        team.addEntry(leader.getName());
    }

    public void addNewMember(Player player){
        this.kingdomPlayers.add(player);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(this.getKingdomName());
        team.addEntry(player.getName());
    }

    public static boolean isKing(Player player){
        for (Kingdom kingdom : kingdoms){
            if (kingdom.kingdomLeader == player){
                return true;
            }
        }
        return false;
    }

    public static Kingdom findLeaderKingdom(Player player){
        for (Kingdom kingdom : kingdoms){
            if (kingdom.kingdomLeader == player){
                return kingdom;
            }
        }
        return null;
    }

    public static void changeColor(String newColor, Kingdom kingdom) {
        // Update kingdom color
        kingdom.setKingdomColor(newColor);

        // Get the scoreboard and team
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(kingdom.getKingdomName());

        if (team != null) {
            // Convert the newColor to a ChatColor and apply it
            team.setPrefix(ColorConverter.stringToColorCode(newColor) + "[" + kingdom.getKingdomName() + "] ");
        }

        List<Zombie> changedFlags = kingdom.kingdomFlags;

        for(Zombie flag : changedFlags){
            List<Entity> headDisplayers = flag.getPassengers();
            for(Entity entityArmorStands : headDisplayers){
                ArmorStand stand = (ArmorStand) entityArmorStands;
                stand.setHelmet(new ItemStack(ColorConverter.stringToBannerColor(newColor)));
            }
            int flagIndex = Flag.findFlagIndex(flag);
            Flag.setBossBarColor(flagIndex, newColor);
        }

        //int flagIndex()



    }



    public static List<Kingdom> getKingdoms() {
        return kingdoms;
    }

    public static void setKingdoms(List<Kingdom> kingdoms) {
        Kingdom.kingdoms = kingdoms;
    }

    public static void addKingdoms(Kingdom k) {
        kingdoms.add(k);
    }

    public String getKingdomName() {
        return kingdomName;
    }

    public void setKingdomName(String kingdomName) {
        this.kingdomName = kingdomName;
    }

    public Player getKingdomLeader() {
        return kingdomLeader;
    }

    public void setKingdomLeader(Player kingdomLeader) {
        this.kingdomLeader = kingdomLeader;
    }

    public List<Player> getKingdomPlayers() {
        return kingdomPlayers;
    }

    public void setKingdomPlayers(List<Player> kingdomPlayers) {
        this.kingdomPlayers = kingdomPlayers;
    }

    public List<Zombie> getKingdomFlags() {
        return kingdomFlags;
    }

    public void setKingdomFlags(List<Zombie> kingdomFlags) {
        this.kingdomFlags = kingdomFlags;
    }

    public String getKingdomColor() {
        return kingdomColor;
    }

    public void setKingdomColor(String kingdomColor) {
        this.kingdomColor = kingdomColor;
    }
}
