package me.itsmas.timedaccess.data;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Permission;
import me.itsmas.timedaccess.util.UtilReason;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager
{
    private final TimedAccess plugin;

    public DataManager(TimedAccess plugin)
    {
        this.plugin = plugin;

        loadData();
    }

    public void save()
    {
        try
        {
            config.save(file);
        }
        catch (IOException ex)
        {
            plugin.getLogger().severe("Error saving data file");
            ex.printStackTrace();
        }
    }

    public long getTimeRemaining(OfflinePlayer player)
    {
        boolean bypass = bypasses(player);

        if (bypass)
        {
            return Long.MIN_VALUE;
        }

        long end = config.getLong(path(player), 0);

        return end - System.currentTimeMillis();
    }

    private boolean bypasses(OfflinePlayer player)
    {
        if (player.isOnline())
        {
            return Permission.hasBypass(player.getPlayer());
        }

        return config.getBoolean(bypass(player));
    }

    public void extendTime(OfflinePlayer player, long time, String reason, CommandSender sender)
    {
        long remaining = getTimeRemaining(player);

        if (remaining <= 0)
        {
            config.set(path(player), System.currentTimeMillis() + time);
        }
        else
        {
            config.set(path(player), System.currentTimeMillis() + remaining + time);
        }
        ArrayList<String> reasons = new ArrayList<>();
        if (config.contains(history(player))) {
            reasons = (ArrayList<String>) config.getStringList(history(player));
        }
        String senderName = "CONSOLE";
        if (sender!=null) {
            senderName = sender.getName();
        }
        UtilReason utilReason = new UtilReason(System.currentTimeMillis(), time, reason, senderName);
        reasons.add(utilReason.createReasonString());
        config.set(history(player), reasons);
    }

    public ArrayList<String> getReasons(OfflinePlayer player) {
        ArrayList<String> reasons = new ArrayList<>();
        if (config.contains(history(player))) {
            reasons = (ArrayList<String>) config.getStringList(history(player));
        }
        return reasons;
    }

    public void remove(OfflinePlayer player)
    {
        config.set(path(player), null);
    }

    public void handleLogin(Player player)
    {
        config.set(bypass(player), Permission.hasBypass(player));
    }

    public boolean hasPlayedBefore(Player player) {
        if (!config.getBoolean(played_before(player))) {
            config.set(played_before(player), true);
            return false;
        }
        return config.getBoolean(played_before(player));
    }

    public void handleQuit(Player player)
    {

    }

    private String path(OfflinePlayer player)
    {
        return player.getUniqueId() + ".end";
    }

    private String bypass(OfflinePlayer player)
    {
        return player.getUniqueId() + ".bypass";
    }
    private String played_before(OfflinePlayer player)
    {
        return player.getUniqueId() + ".played_before";
    }

    private String history(OfflinePlayer player)
    {
        return player.getUniqueId() + ".history";
    }

    private File file;
    private YamlConfiguration config;

    private void loadData()
    {
        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException ex)
            {
                plugin.getLogger().severe("Error creating data file");
                ex.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }
}
