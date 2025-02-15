package me.verosity.knightfall.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class FlagItem extends CustomItem {
    public FlagItem() {
        List<String> flagDescription = new ArrayList<>();
        flagDescription.add("Right Click to place gain a new flag.");

        super("§6Flag", flagDescription, Material.COMMAND_BLOCK, 1);
    }
}
