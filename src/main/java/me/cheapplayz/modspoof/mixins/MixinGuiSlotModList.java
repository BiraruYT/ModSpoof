package me.cheapplayz.modspoof.mixins;

import me.cheapplayz.modspoof.ModSpoof;
import net.minecraftforge.fml.client.GuiSlotModList;
import net.minecraftforge.fml.common.ModContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiSlotModList.class)
public class MixinGuiSlotModList {
    @Unique
    private String modspoof$stringPrefix;

    @ModifyVariable(
            method = "drawSlot",
            at = @At("STORE"),
            ordinal = 0,
            remap = false
    )
    private ModContainer drawSlot(ModContainer mc) {
        if ("FML".equals(mc.getModId()) || "Forge".equals(mc.getModId()) || "mcp".equals(mc.getModId())) {
            modspoof$stringPrefix = "§2✔ §r";
        } else if ("modspoof".equals(mc.getModId())) {
            modspoof$stringPrefix = "§4✖ §r";
        } else {
            if (ModSpoof.states.get(mc.getModId())) {
                modspoof$stringPrefix = "§c✖ §r";
            } else {
                modspoof$stringPrefix = "§a✔ §r";
            }
        }
        return mc;
    }

    @ModifyVariable(
            method = "drawSlot",
            at = @At("STORE"),
            ordinal = 0,
            remap = false
    )
    private String drawSlot2(String name) {
        return modspoof$stringPrefix + name;
    }
}