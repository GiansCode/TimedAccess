package me.itsmas.timedaccess;

import me.itsmas.timedaccess.command.MainCommand;
import me.itsmas.timedaccess.data.DataManager;
import me.itsmas.timedaccess.listener.LoginListener;
import me.itsmas.timedaccess.placeholder.PlaceholderHook;
import me.itsmas.timedaccess.task.TimeCheckTask;
import me.itsmas.timedaccess.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TimedAccess extends JavaPlugin
{
    private DataManager dataManager;
    private int default_time;

    @Override
    public void onEnable()
    {
        preInit();

        dataManager = new DataManager(this);
        default_time = getConfig().getInt("default_playtime");

        new LoginListener(this);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook(this).register();
        }
        new TimeCheckTask(this);

        getCommand("timedaccess").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable()
    {
        getDataManager().save();
    }

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public int getDefaultTime() {
        return default_time;
    }

    private void preInit()
    {
        saveDefaultConfig();
        Message.init(this);
    }
}
