package me.itsmas.timedaccess.util;

import me.itsmas.timedaccess.TimedAccess;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilServer
{
    private static final TimedAccess PLUGIN = JavaPlugin.getPlugin(TimedAccess.class);

    public static void registerListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }
}
