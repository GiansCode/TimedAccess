package me.itsmas.timedaccess;

import me.itsmas.timedaccess.command.MainCommand;
import me.itsmas.timedaccess.data.DataManager;
import me.itsmas.timedaccess.listener.LoginListener;
import me.itsmas.timedaccess.placeholder.PlaceholderHook;
import me.itsmas.timedaccess.task.TimeCheckTask;
import me.itsmas.timedaccess.util.Message;
import org.bukkit.plugin.java.JavaPlugin;

public class TimedAccess extends JavaPlugin
{
    private DataManager dataManager;

    @Override
    public void onEnable()
    {
        preInit();

        dataManager = new DataManager(this);

        new LoginListener(this);
        new PlaceholderHook(this).hook();

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

    private void preInit()
    {
        saveDefaultConfig();
        Message.init(this);
    }
}
