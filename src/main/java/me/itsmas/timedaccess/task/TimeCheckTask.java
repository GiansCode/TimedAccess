package me.itsmas.timedaccess.task;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Colour;
import me.itsmas.timedaccess.util.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.MessageFormat;
import java.util.List;

public class TimeCheckTask extends BukkitRunnable
{
    private final TimedAccess plugin;

    private final List<String> kickCommands;

    public TimeCheckTask(TimedAccess plugin)
    {
        this.plugin = plugin;

        List<String> kickCommands = plugin.getConfig().getStringList("kick_commands");
        kickCommands.replaceAll(Colour::translate);

        this.kickCommands = kickCommands;

        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            long remaining = plugin.getDataManager().getTimeRemaining(player);
            boolean bypass = Permission.hasBypass(player);

            if (!bypass && remaining <= 0)
            {
                // No time left, doesn't have bypass
                kickCommands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageFormat.format(command, player.getName())));
            }
        }
    }
}
