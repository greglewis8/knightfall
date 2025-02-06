package me.verosity.knightfall;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import org.bukkit.World;
import org.bukkit.util.EulerAngle;

public class Flag {

    private static final List<Zombie> flags = new ArrayList<>();
    private static final List<Double> flagsHP = new ArrayList<>();

    private static final double flagMaxHP = 100.0;
    private static final double flagMinHP = 0.0;

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

        // 4 raids armorStand.setGlowing(true);

        // Rotate the head downwards
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(180), 0, 0));

        // Make the armor stand a passenger of the zombie
        flag.addPassenger(armorStand);

        flag.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4, false, false));

        flags.add(flag);
        flagsHP.add(flagMaxHP);

        return flag;
    }

    public static boolean isFlag(Zombie zombie) {
        return flags.contains(zombie); // Check if the zombie is a tracked flag
    }


    public static void damageFlag(Zombie damagedFlagZombie){
        int damagedFlagIndex = flags.indexOf(damagedFlagZombie);
        if(damagedFlagIndex > -1){
            if(flagsHP.get(damagedFlagIndex) > flagMinHP){
                flagsHP.set(damagedFlagIndex, flagsHP.get(damagedFlagIndex) - 1);
            }
        }


    }

    public static double getFlagHP(Zombie zombie) {
        int index = flags.indexOf(zombie);
        if (index > -1) {
            return flagsHP.get(index);
        }
        return -1;
    }


}
