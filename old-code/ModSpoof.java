package me.cheapplayz.modspoof;

import me.cheapplayz.modspoof.commands.ModSpoofCommand;
import me.cheapplayz.modspoof.core.Config;
import me.cheapplayz.modspoof.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

@Mod(
        modid = ModSpoof.MOD_ID,
        name = ModSpoof.MOD_NAME,
        version = ModSpoof.VERSION
)

public class ModSpoof {
    public static final String MOD_ID = "modspoof";
    public static final String MOD_NAME = "ModSpoof";
    public static final String VERSION = "1.0";
    public static final String configLocation = "config/modspoof.toml";
    public static String[] blacklistedModIds = {"Forge", "FML", "mcp", "modspoof"};
    public static Config config = null;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static GuiCheckBox checkbox = null;
    public static HashMap<String, Boolean> states = new HashMap<>();
    public static Field listWidth;
    public static Field selectedMod;

    static {
        try {
            listWidth = GuiModList.class.getDeclaredField("listWidth");
            selectedMod = GuiModList.class.getDeclaredField("selectedMod");
        } catch (NoSuchFieldException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        listWidth.setAccessible(true);
        selectedMod.setAccessible(true);
        MinecraftForge.EVENT_BUS.register(this);
        new ModSpoofCommand().register();
        config = new Config(new File(configLocation));
        config.preload();
        HashMap<String, Boolean> savedStates = Utils.loadConfig();
        Loader.instance().getModList().forEach(modContainer -> {
            states.put(modContainer.getModId(), savedStates.containsKey(modContainer.getModId()));
        });
    }
}