package me.cheapplayz.modspoof.utils;

import me.cheapplayz.modspoof.ModSpoof;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<String> loadConfig() {
        return ModSpoof.config != null ? Arrays.asList(ModSpoof.config.hiddenMods.split("\n")) : Arrays.asList();
    }

    public static void saveConfig() {
        StringBuilder modstring = new StringBuilder();

        for (String modid : ModSpoof.states.keySet()) {
            boolean hidden = ModSpoof.states.get(modid);
            if (hidden) {
                modstring.append(modid).append("\n");
            }
        }

        if (ModSpoof.config != null) {
            ModSpoof.config.hiddenMods = modstring.toString();
            ModSpoof.config.markDirty();
            ModSpoof.config.writeData();
            System.out.println("[ModSpoof] Saved Config.");
        }
    }

    public static void chat(String msg) {
        if (ModSpoof.mc.player != null) {
            ModSpoof.mc.player.sendMessage(new TextComponentString(msg));
        }
    }

    public static boolean containsIgnoreCase(String[] array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}