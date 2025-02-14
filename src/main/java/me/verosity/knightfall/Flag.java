package me.verosity.knightfall;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
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

public class Flag implements Listener{
    private static List<Zombie> flags = new ArrayList<>();
    private static List<Double> flagsHP = new ArrayList<>();
    private static List<Kingdom> flagsOwner = new ArrayList<>();

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

    public static Zombie spawnFlag(Location loc, Kingdom kingdom) {
        World world = loc.getWorld();
        if (world == null) return null;

        world.loadChunk(loc.getChunk());

        Zombie flag = (Zombie) world.spawn(loc, Zombie.class);
        flag.setCustomName("flag");
        flag.setCustomNameVisible(true);
        flag.setInvisible(true);
        flag.setAI(false);
        flag.setGravity(false);

        flag.getEquipment().clear();
        flag.getEquipment().setHelmet(null);
        flag.getEquipment().setChestplate(null);
        flag.getEquipment().setLeggings(null);
        flag.getEquipment().setBoots(null);
        flag.getEquipment().setItemInMainHand(null);
        flag.getEquipment().setItemInOffHand(null);

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
        flag.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));

        flag.setCustomName(kingdom.getKingdomName());

        flags.add(flag);
        flagsHP.add(flagMaxHP);
        flagsOwner.add(kingdom);

        createFlagBar();


        return flag;
    }

    /// Flag Modifications

    public static void flagMove(Zombie flag, Location newLocation) {
        if (flag == null || newLocation == null) return;

        World world = flag.getWorld();
        Location oldLocation = flag.getLocation();

        Chunk chunk = world.getChunkAt(newLocation);
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        List<Entity> passengers = flag.getPassengers();
        for (Entity entity : passengers) {
            if (entity instanceof ArmorStand) {
                //entity.teleport(newLocation);
                flag.removePassenger(entity);
                Bukkit.getLogger().info("ArmorStand Teleported to: " + newLocation.toString());
            }
        }

        flag.setAI(true);
        flag.setGravity(true); // Allow gravity to prevent glitches during teleport
        flag.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        newLocation.getChunk().load();
        flag.setAI(false);
        flag.setGravity(false); // Restore original settings

        for (Entity entity : passengers) {
            if (entity instanceof ArmorStand) {
                entity.teleport(newLocation);
                flag.addPassenger(entity);
            }
        }

        oldLocation.getChunk().unload();
    }

    public static void deleteFlag(Zombie flag) {
        int flagIndex = flags.indexOf(flag);

        if (flagIndex != -1) {
            // Remove from lists
            flags.remove(flagIndex);
            flagsHP.remove(flagIndex);
            flagsOwner.remove(flagIndex);

            // Remove boss bar if it exists
            if (flagIndex < flagBossBars.size()) {
                flagBossBars.get(flagIndex).removeAll();
                flagBossBars.remove(flagIndex);
            }

            // Remove flag entity from the world
            List <Entity> flagPassengers = flag.getPassengers();
            for (Entity x : flagPassengers){
                x.remove();
            }

            World flagWorld = flag.getWorld();
            Chunk flagChunk = flag.getLocation().getChunk();


            flag.remove();
            flagWorld.unloadChunk(flagChunk);

            Bukkit.getLogger().info("Flag removed successfully.");
        } else {
            Bukkit.getLogger().warning("Attempted to delete a non-existent flag.");
        }
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
        updateFlagBar(healedFlagIndex);
    }

    public static int findFlagIndex(Zombie flag){
        for (int i = 0; i < flags.size(); i++){
            if (flag.equals(flags.get(i))){
                return i;
            }
        }
        return -1;
    }


    /// BossBar Stuff

    private static final List<BossBar> flagBossBars = new ArrayList<>();

    private static void createFlagBar(){
        BossBar flagBar = Bukkit.createBossBar("unknown's Flag Health : 100%", BarColor.RED, BarStyle.SEGMENTED_10);
        flagBossBars.add(flagBar);
    }

    private static void updateFlagBar(int i) {
        if (i < 0 || i >= flags.size()) return; // Ensure the index is valid
        if (flags.isEmpty()) {
            System.out.println("No flags found. Skipping update.");
            return;
        }
        BossBar flag = flagBossBars.get(i);
        double doubleFlagHP = flagsHP.get(i);
        int intFlagHP = (int) doubleFlagHP;

        flag.setTitle(ColorConverter.convertToColorString(flagsOwner.get(i).getKingdomColor()) + flagsOwner.get(i).getKingdomName() +"§r Flag Health : " + intFlagHP + "%");
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

    public static void setBossBarColor(int flagIndex, String newBannerColor){
        BossBar bossBar = flagBossBars.get(flagIndex);
        bossBar.setColor(ColorConverter.stringToBossBarColor(newBannerColor));
        updateFlagBar(flagIndex);
    }


    private static final double distanceThreshold = 100.0;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        if (flags.isEmpty()) return; // Return early if no flags exist

        Location playerLocation = player.getLocation();
        double thresholdSquared = distanceThreshold * distanceThreshold; // Use squared threshold

        for (int i = 0; i < flags.size(); i++) {
            Zombie flag = flags.get(i);
            double distanceSquared = playerLocation.distanceSquared(flag.getLocation()); // Avoid expensive sqrt()

            if (distanceSquared <= thresholdSquared) {
                showBossBar(i, player);
            } else {
                removeBossBar(i, player);
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
                flagData.flagEntries.add(new FlagEntry(flag.getUniqueId(), flag.getLocation(), flagsHP.get(i), flagsOwner.get(i).getKingdomName()));
            }
            writer.write(gson.toJson(flagData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization - Load Flags from JSON
    public static void loadFlagsFromFile(File file) {
        if (!file.exists()) return;

        try {
            String json = new String(Files.readAllBytes(Paths.get(file.toURI())));
            FlagData flagData = gson.fromJson(json, FlagData.class);

            flags.clear();
            flagsHP.clear();
            flagsOwner.clear();

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
                            String flagKingdomName = entry.flagOwner;

                            List<Kingdom> kingdoms = Kingdom.getKingdoms();
                            for(Kingdom possibleKingdom : kingdoms){
                                if (possibleKingdom.getKingdomName().equals(flagKingdomName)){
                                    flagsOwner.add(possibleKingdom);
                                }
                            }


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
        String flagOwner;

        public FlagEntry(UUID uuid, Location loc, double health, String flagOwner) {
            this.uuid = uuid;
            this.world = loc.getWorld().getName();
            this.x = loc.getX();
            this.y = loc.getY();
            this.z = loc.getZ();
            this.health = health;
            this.flagOwner = flagOwner;
        }
    }
}
