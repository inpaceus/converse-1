package net.novelmc.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminchatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("converse.adminchat")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        String message = StringUtils.join(args, " ");
        if (message.length() == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Adminchat can only be toggled in game.");
                return true;
            }
            Player p = (Player) sender;
            Util.putAdminChat(p.getUniqueId());
            p.sendMessage("Toggled adminchat " + (Util.isInAdminChat(p.getUniqueId()) ? "on" : "off"));
            return true;
        }
        Util.adminchat(sender, message);
        return true;
    }
}
