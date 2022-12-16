package me.itsmas.timedaccess.listener;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Message;
import me.itsmas.timedaccess.util.Permission;
import me.itsmas.timedaccess.util.UtilServer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginListener implements Listener
{
    private final TimedAccess plugin;

    public LoginListener(TimedAccess plugin)
    {
        this.plugin = plugin;

        UtilServer.registerListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event)
    {
        Player player = event.getPlayer();

        plugin.getDataManager().handleLogin(player);

        if (!plugin.getDataManager().hasPlayedBefore(player)) {
            plugin.getDataManager().extendTime(player, plugin.getDefaultTime(), "Added default time", null);
            return;
        }

        if (Permission.hasBypass(player))
        {
            return;
        }

        long remaining = plugin.getDataManager().getTimeRemaining(player);

        if (remaining != Long.MIN_VALUE && remaining <= 0)
        {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Message.KICK_NO_TIME.format());
        }
        else
        {
            plugin.getDataManager().handleLogin(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        plugin.getDataManager().handleQuit(event.getPlayer());
    }
}
