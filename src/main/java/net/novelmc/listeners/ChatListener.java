package net.novelmc.listeners;

import java.util.ArrayList;
import java.util.List;
import net.novelmc.Converse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private Converse plugin;

    public ChatListener(Converse plugin)
    {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static List<Player> punished = new ArrayList<>();

    public static boolean isPunished(Player player)
    {
        return punished.contains(player);
    }

    public static void setPunished(Player player, boolean mute)
    {
        if (mute)
        {
            punished.add(player);
            return;
        }
        punished.remove(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (isPunished(player))
        {
            event.setMessage(ChatColor.BLACK + message);
        }
        if (message.equalsIgnoreCase("i steal source code"))
        {
            player.sendMessage(ChatColor.GREEN + "Congratulations! You are now a NovelMC developer!");
            plugin.lp.setPrefix(player.getUniqueId(), "prefix.1.&5&lDEV &7");
        }
    }
}
