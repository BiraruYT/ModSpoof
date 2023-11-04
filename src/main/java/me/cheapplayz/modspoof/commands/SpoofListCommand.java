package me.cheapplayz.modspoof.commands;

import me.cheapplayz.modspoof.utils.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.util.text.TextFormatting;

public class SpoofListCommand implements ICommand {
    @Override
    public String getName() {
        return "spooflist";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/spooflist";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = Lists.<String>newArrayList();
        aliases.add("/spooflist");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String mods = "None";
        if (!(args.length > 1)) {
            mods = String.join("§7, §3", Utils.loadConfig());
            sender.sendMessage(new TextComponentString("§8[§3ModSpoof§8] §7Hidden Mods: §3" + mods));
        }
        else {
            sender.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "Usage: /spooflist"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}