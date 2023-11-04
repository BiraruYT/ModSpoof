package me.cheapplayz.modspoof.core;

import me.cheapplayz.modspoof.ModSpoof;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    private Configuration config;

    public String hiddenMods = "";

    public Config() {
        config = new Configuration(new File(ModSpoof.configLocation));
        loadConfig();
    }

    public void loadConfig() {
        config.load();

        // Load hidden mods from the configuration file
        hiddenMods = config.getString("Hidden Mods", Configuration.CATEGORY_GENERAL, "", "List of hidden mods");

        config.save();
    }

    public void saveConfig() {
        config.load();

        // Save hidden mods to the configuration file
        config.get(Configuration.CATEGORY_GENERAL, "Hidden Mods", "").set(hiddenMods);

        config.save();
    }

    public void writeData() {
        saveConfig();
    }

    public void markDirty() {
        config.hasChanged();
    }
}