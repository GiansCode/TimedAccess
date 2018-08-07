package me.itsmas.timedaccess.command;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Message;
import me.itsmas.timedaccess.util.Permission;
import me.itsmas.timedaccess.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor
{
    private final TimedAccess plugin;

    public MainCommand(TimedAccess plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        handleCommand(sender, args);
        return true;
    }

    private void handleCommand(CommandSender sender, String[] args)
    {
        if (!Permission.check(sender, "timedaccess.command"))
        {
            return;
        }

        if (args.length == 0)
        {
            Message.COMMAND_USAGE.send(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("give"))
        {
            handleGive(sender, args);
        }
        else if (args[0].equalsIgnoreCase("get"))
        {
            handleGet(sender, args);
        }
        else
        {
            Message.COMMAND_USAGE.send(sender);
        }
    }

    private void handleGive(CommandSender sender, String[] args)
    {
        if (!Permission.check(sender, "timedaccess.command.give"))
        {
            return;
        }

        if (args.length != 3)
        {
            Message.COMMAND_GIVE_USAGE.send(sender);
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

        long time = UtilTime.parseTime(args[2]);

        if (time <= 0L)
        {
            Message.INVALID_TIME.send(sender);
            return;
        }

        plugin.getDataManager().extendTime(player, time);

        String formatted = UtilTime.toHms(time);
        Message.COMMAND_GIVE_SUCCESS.send(sender, player.getName(), formatted);

        if (player.isOnline())
        {
            Message.COMMAND_GET_RECEIVED.send(player.getPlayer(), sender.getName(), formatted);
        }
    }

    private void handleGet(CommandSender sender, String[] args)
    {
        if (!Permission.check(sender, "timedaccess.command.get"))
        {
            Message.COMMAND_GET_USAGE.send(sender);
            return;
        }

        if (args.length != 2)
        {
            Message.COMMAND_GET_USAGE.send(sender);
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

        String time = UtilTime.toHms(plugin.getDataManager().getTimeRemaining(player));
        Message.COMMAND_GET_SUCCESS.send(sender, player.getName(), time);
    }

    private boolean checkPlayer(Player player, CommandSender sender)
    {
        if (player == null)
        {
            Message.PLAYER_OFFLINE.send(sender);
            return false;
        }

        return true;
    }
}
