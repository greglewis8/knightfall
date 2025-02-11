package me.verosity.knightfall;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Kingdom {

    private static List<Kingdom> kingdoms = new ArrayList<>();

    private String kingdomName;
    private Player kingdomLeader;
    private List<Player> kingdomPlayers;
    private Color kingdomColor;
    private List<Zombie> kingdomFlags;

    public Kingdom(String name, Player leader, boolean spawnFlag) {
        kingdomName = name;
        kingdomLeader = leader;
        kingdomPlayers = new ArrayList<Player>();
        kingdomColor = Color.WHITE;
        kingdomFlags = new ArrayList<Zombie>();

        kingdoms.add(this);

        if (spawnFlag) {
            Zombie flag = Flag.spawnFlag(kingdomLeader.getLocation(), this);
            kingdomFlags.add(flag);
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.registerNewTeam(kingdomName);
        team.setPrefix(ChatColor.WHITE + "[" + kingdomName + "] ");
        leader.sendMessage(ChatColor.GREEN + "Kingdom " + kingdomName + " has been created!");
        team.addEntry(leader.getName());
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

    public Color getKingdomColor() {
        return kingdomColor;
    }

    public void setKingdomColor(Color kingdomColor) {
        this.kingdomColor = kingdomColor;
    }
}
