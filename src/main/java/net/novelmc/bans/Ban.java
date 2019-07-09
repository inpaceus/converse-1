package net.novelmc.bans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import net.novelmc.Converse;
import net.novelmc.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ban
{
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    private Converse plugin;

    public Ban(Converse plugin)
    {
        this.plugin = plugin;
    }

    public String constructBanMessage(Player player, String reason, String banID)
    {
        final StringBuilder banMessage = new StringBuilder(ChatColor.BLUE + "" + ChatColor.BOLD
                + "NOVEL");
        banMessage.append(ChatColor.WHITE + "" + ChatColor.BOLD + "MC\n");
        banMessage.append(ChatColor.RED + "You are temporarily banned from this server!\n");
        if (!(reason.length() == 0))
        {
            banMessage.append(ChatColor.DARK_GRAY + "Reason: " + ChatColor.GRAY + getReason(player) + "\n");
        }
        banMessage.append(ChatColor.DARK_GRAY + "Until: " + ChatColor.GRAY + formatDate(player)).append("\n");
        banMessage.append(ChatColor.DARK_GRAY + "More Info: " + ChatColor.GRAY + plugin.config.getString("banurl") + "\n");
        banMessage.append(ChatColor.DARK_GRAY + "Ban ID: " + ChatColor.GRAY + "#" + banID);
        return banMessage.toString();
    }

    public void addBan(Player player, CommandSender sender, String banID, String reason, Date duration, String type)
    {
        plugin.banConfig.createSection(player.getUniqueId().toString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".ip", player.getAddress().getHostString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.banConfig.set(player.getUniqueId().toString() + ".duration", duration);
        plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.banConfig.save();
        plugin.banConfig.load();
    }

    public void addBan(OfflinePlayer player, CommandSender sender, String banID, String reason, Date duration, String type)
    {
        plugin.banConfig.createSection(player.getUniqueId().toString());
        plugin.banConfig.set(player.getUniqueId().toString() + ".player", player.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".type", type);
        plugin.banConfig.set(player.getUniqueId().toString() + ".by", sender.getName());
        plugin.banConfig.set(player.getUniqueId().toString() + ".reason", reason);
        plugin.banConfig.set(player.getUniqueId().toString() + ".duration", duration);
        plugin.banConfig.set(player.getUniqueId().toString() + ".id", banID);
        plugin.banConfig.save();
        plugin.banConfig.load();
    }

    public String getReason(Player player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public String getReason(OfflinePlayer player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".reason").toString();
    }

    public boolean hasReason(OfflinePlayer player)
    {
        return plugin.banConfig.isSet(player.getUniqueId().toString() + ".reason");
    }

    public String getIP(OfflinePlayer player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".ip").toString();
    }

    public boolean hasIP(OfflinePlayer player)
    {
        return plugin.banConfig.isSet(player.getUniqueId().toString() + ".ip");
    }

    public String getBannedBy(OfflinePlayer player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".by").toString();
    }

    public String getBanID(Player player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public String getBanID(OfflinePlayer player)
    {
        return plugin.banConfig.get(player.getUniqueId().toString() + ".id").toString();
    }

    public long getExpireUnix(Player player)
    {
        return Util.getUnixTime((Date)plugin.banConfig.get(player.getUniqueId() + ".duration"));
    }

    public long getExpireUnix(OfflinePlayer player)
    {
        return Util.getUnixTime((Date)plugin.banConfig.get(player.getUniqueId() + ".duration"));
    }

    public String formatDate(Player player)
    {
        return simpleDateFormat.format(Util.getUnixDate(getExpireUnix(player)));
    }

    public String formatDate(OfflinePlayer player)
    {
        return simpleDateFormat.format(Util.getUnixDate(getExpireUnix(player)));
    }


    public boolean hasExpiry(Player player)
    {
        return getExpireUnix(player) > 0;
    }

    public boolean hasExpiry(OfflinePlayer player)
    {
        return getExpireUnix(player) > 0;
    }

    public boolean isExpired(Player player)
    {
        return hasExpiry(player) && getExpireUnix(player) < Util.getUnixTime();
    }

    public boolean getPlayer(Player player)
    {
        return plugin.banConfig.isConfigurationSection(player.getUniqueId().toString());
    }

    public boolean getPlayer(UUID uuid)
    {
        return plugin.banConfig.isConfigurationSection(uuid.toString());
    }

    public boolean isBanned(Player player)
    {
        final long expire = getExpireUnix(player);
        return expire != 0 && Util.getUnixTime() > expire;
    }

    public boolean removeBan(OfflinePlayer player)
    {
        if (plugin.banConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.banConfig.set(player.getUniqueId().toString(), null);
            plugin.banConfig.save();
            plugin.banConfig.load();
            return true;
        }
        return false;
    }

    public boolean removeBan(Player player)
    {
        if (plugin.banConfig.isConfigurationSection(player.getUniqueId().toString()))
        {
            plugin.banConfig.set(player.getUniqueId().toString(), null);
            plugin.banConfig.save();
            plugin.banConfig.load();
            return true;
        }
        return false;
    }
}
