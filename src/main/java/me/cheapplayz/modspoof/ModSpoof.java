package me.cheapplayz.modspoof;

import me.cheapplayz.modspoof.commands.SpoofListCommand;
import me.cheapplayz.modspoof.core.Config;
import me.cheapplayz.modspoof.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.ClientCommandHandler;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public static Config config;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static GuiCheckBox checkbox;
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
    public void init(FMLInitializationEvent event) throws NoSuchFieldException {
        listWidth = GuiModList.class.getDeclaredField("listWidth");
        selectedMod = GuiModList.class.getDeclaredField("selectedMod");
        listWidth.setAccessible(true);
        selectedMod.setAccessible(true);

        MinecraftForge.EVENT_BUS.register(this);

        config = new Config();
        config.loadConfig();

        for (ModContainer modContainer : Loader.instance().getModList()) {
            states.put(modContainer.getModId(), Utils.loadConfig().contains(modContainer.getModId()));
        }

        if (event.getSide() == Side.CLIENT) {
            registerClientCommands();
        }
    }

    @SideOnly(Side.CLIENT)
    private void registerClientCommands() {
        ClientCommandHandler.instance.registerCommand(new SpoofListCommand());
    }

    @SubscribeEvent
    public void initGui(InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiModList) {
            GuiModList gui = (GuiModList) event.getGui();
            int width = 0;
            try {
                width = listWidth.getInt(gui);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            checkbox = new GuiCheckBox(555, width + 15, gui.height - 23, "Hide Mod", false);
            checkbox.visible = false;

            event.getButtonList().add(checkbox);
        }
    }

    @SubscribeEvent
    public void actionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiModList && event.getButton() instanceof GuiCheckBox && event.getButton().id == 555) {
            GuiCheckBox button = (GuiCheckBox) event.getButton();
            GuiModList gui = (GuiModList) event.getGui();
            Object selected = null;
            try {
                selected = selectedMod.get(gui);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (selected instanceof ModContainer) {
                ModContainer modContainer = (ModContainer) selected;
                states.put(modContainer.getModId(), button.isChecked());
            }
        }
    }

    @SubscribeEvent
    public void guiClick(GuiScreenEvent.MouseInputEvent.Post event) {
        if (event.getGui() instanceof GuiModList) {
            GuiModList gui = (GuiModList) event.getGui();
            Object selected = null;
            try {
                selected = selectedMod.get(gui);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (selected instanceof ModContainer) {
                ModContainer modContainer = (ModContainer) selected;

                if (Utils.containsIgnoreCase(blacklistedModIds, modContainer.getModId())) {
                    checkbox.visible = false;
                    return;
                }

                checkbox.visible = true;
                checkbox.setIsChecked(states.getOrDefault(modContainer.getModId(), false));
            }
        }
    }

    @SubscribeEvent
    public void guiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            Utils.saveConfig();
        }
    }
}