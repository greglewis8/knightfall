package me.verosity.knightfall;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static java.lang.String.format;

public class Flag implements Listener{
    private static List<Zombie> flags = new ArrayList<>();
    private static List<Double> flagsHP = new ArrayList<>();

    public static List<Zombie> getFlags() {
        return flags;
    }

    public static List<Double> getFlagsHP() {
        return flagsHP;
    }

    public static void setFlags(List<Zombie> flags) {
        Flag.flags = flags;
    }

    public static void setFlagsHP(List<Double> flagsHP) {
        Flag.flagsHP = flagsHP;
    }

    private static final double flagMaxHP = 100.0;
    private static final double flagMinHP = 0.0;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Zombie spawnFlag(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;

        Zombie flag = (Zombie) world.spawn(loc, Zombie.class);
        flag.setCustomName("flag");
        flag.setCustomNameVisible(true);
        flag.setInvisible(true);
        flag.setAI(false);
        flag.setGravity(false);

        // Spawn an invisible armor stand
        ArmorStand armorStand = (ArmorStand) world.spawn(loc, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setSmall(true);

        armorStand.setHelmet(new ItemStack(Material.WHITE_BANNER));
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));
        flag.addPassenger(armorStand);

        flag.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4, false, false));

        flags.add(flag);
        flagsHP.add(flagMaxHP);
        createFlagBar();

        return flag;
    }

    public static boolean isFlag(Zombie zombie) {
        return flags.contains(zombie);
    }

    public static void damageFlag(Zombie damagedFlagZombie) {
        int damagedFlagIndex = flags.indexOf(damagedFlagZombie);
        if (damagedFlagIndex > -1) {
            if (flagsHP.get(damagedFlagIndex) > flagMinHP) {
                flagsHP.set(damagedFlagIndex, flagsHP.get(damagedFlagIndex) - 1);
                updateFlagBar(damagedFlagIndex);
            }
        }
    }

    public static void healFlag(Zombie healedFlagZombie, Double healAmount) {
        int healedFlagIndex = flags.indexOf(healedFlagZombie);
        if (healedFlagIndex > -1) {
            if (flagsHP.get(healedFlagIndex) < flagMaxHP - healAmount) {
                flagsHP.set(healedFlagIndex, flagsHP.get(healedFlagIndex) + healAmount);
            } else {
                flagsHP.set(healedFlagIndex, flagMaxHP);
            }
        }
    }

    public static double findFlagHP(Zombie zombie) {
        int index = flags.indexOf(zombie);
        if (index > -1) {
            return flagsHP.get(index);
        }

        return -1.0;
    }


    /// BossBar Stuff

    private static final List<BossBar> flagBossBars = new ArrayList<>();

    private static void createFlagBar(){
        BossBar flagBar = Bukkit.createBossBar("Flag Health : 100%", BarColor.RED, BarStyle.SEGMENTED_10);
        flagBossBars.add(flagBar);
    }

    private static void updateFlagBar(int i) {
        if (i < 0 || i >= flags.size()) return; // Ensure the index is valid
        BossBar flag = flagBossBars.get(i);
        double doubleFlagHP = flagsHP.get(i);
        int intFlagHP = (int) doubleFlagHP;

        flag.setTitle("Flag Health : " + intFlagHP + "%");
        flag.setProgress(doubleFlagHP / 100);
    }

    private static void showBossBar(int flagIndex, Player player){
        BossBar bossBar = flagBossBars.get(flagIndex);
        if (!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);  // Add the player to the boss bar
        }
    }

    public static void removeBossBar(int flagIndex, Player player){
        BossBar bossBar = flagBossBars.get(flagIndex);
        if (bossBar.getPlayers().contains(player)) {
            bossBar.removePlayer(player);  // Add the player to the boss bar
        }
    }


    private static final double distanceThreshold = 100.0;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        for(int i = 0; i < flagBossBars.size(); i++){
            Zombie targetEntity = flags.get(i);
            double distance = player.getLocation().distance(targetEntity.getLocation());

            if(distance <= distanceThreshold){
                showBossBar(i,player);
            } else {
                removeBossBar(i,player);
            }
        }

    }


    /// File Saving

    // Serialization - Save Flags to JSON
    public static void saveFlagsToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            FlagData flagData = new FlagData();
            for (int i = 0; i < flags.size(); i++) {
                Zombie flag = flags.get(i);
                flagData.flagEntries.add(new FlagEntry(flag.getUniqueId(), flag.getLocation(), flagsHP.get(i)));
            }
            writer.write(gson.toJson(flagData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization - Load Flags from JSON
    // Deserialization - Load Flags from JSON
    public static void loadFlagsFromFile(File file) {
        if (!file.exists()) return;

        try {
            String json = new String(Files.readAllBytes(Paths.get(file.toURI())));
            FlagData flagData = gson.fromJson(json, FlagData.class);

            flags.clear();
            flagsHP.clear();

            for (FlagEntry entry : flagData.flagEntries) {
                World world = Bukkit.getWorld(entry.world);
                if (world == null) continue;
                Location loc = new Location(world, entry.x, entry.y, entry.z);
                Chunk chunk = loc.getChunk();

                // Ensure the chunk is loaded
                world.loadChunk(chunk);

                double flagHP = entry.health;
                // Get all entities in the chunk
                Entity[] possibleFlags = chunk.getEntities();

                System.out.println(Arrays.toString(possibleFlags));

                for (Entity possibleflag : possibleFlags) {
                    if (possibleflag instanceof Zombie) {
                        Zombie flagZombie = (Zombie) possibleflag;
                        String possibleflagUUID = possibleflag.getUniqueId().toString();
                        String flagUUID = entry.uuid.toString();

                        // Compare UUIDs to check if it's the correct flag
                        if (possibleflagUUID.equals(flagUUID)) {
                            flags.add(flagZombie);
                            flagsHP.add(flagHP);
                            createFlagBar();
                            System.out.println("FLAG FOUND!");

                            int flagIndex = flags.size()-1;
                            updateFlagBar(flagIndex);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Inner class for storing flag data in JSON
    private static class FlagData {
        List<FlagEntry> flagEntries = new ArrayList<>();
    }

    private static class FlagEntry {
        UUID uuid;
        String world;
        double x, y, z;
        double health;

        public FlagEntry(UUID uuid, Location loc, double health) {
            this.uuid = uuid;
            this.world = loc.getWorld().getName();
            this.x = loc.getX();
            this.y = loc.getY();
            this.z = loc.getZ();
            this.health = health;
        }
    }
}
