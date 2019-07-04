package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;
import net.novelmc.Converse;
import net.novelmc.util.ConverseBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MuteListener extends ConverseBase implements Listener
{
    private static List<Player> muted = new ArrayList<>();
    private final static List<String> blockedCommands = Converse.plugin.config.getStringList("muted_commands");

    public static boolean isMuted(Player player)
    {
        return muted.contains(player);
    }

    public static void setMuted(Player player, boolean mute)
    {
        if (mute)
        {
            muted.add(player);
            return;
        }
        muted.remove(player);
    }

    public static int getMutedAmount()
    {
        return muted.size();
    }

    public static void purgeMuted()
    {
        muted.clear();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();

        if (plugin.lp.isStaff(player.getUniqueId()))
        {
            return;
        }

        if (isMuted(player))
        {
            String command = event.getMessage().split(" ")[0].toLowerCase();

            if (command.startsWith("/"))
            {
                command = command.substring(1);
            }

            Command bukkitCommand = Bukkit.getServer().getPluginCommand(command);

            if (bukkitCommand != null)
            {
                command = bukkitCommand.getName().toLowerCase();
            }

            if (blockedCommands.contains(command))
            {
                player.sendMessage(ChatColor.RED + "You cannot use that command while you are muted.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (plugin.lp.isStaff(player.getUniqueId()))
        {
            return;
        }
        if (isMuted(player))
        {
            player.sendMessage(ChatColor.RED + "You are currently muted and cannot chat.");
            event.setCancelled(true);
        }
    }
}
