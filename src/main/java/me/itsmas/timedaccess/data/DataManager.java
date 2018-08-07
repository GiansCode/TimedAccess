package me.itsmas.timedaccess.data;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

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

    public void extendTime(OfflinePlayer player, long time)
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
    }

    public void handleLogin(Player player)
    {
        config.set(bypass(player), Permission.hasBypass(player));
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
