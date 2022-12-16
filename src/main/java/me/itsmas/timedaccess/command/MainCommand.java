package me.itsmas.timedaccess.command;

import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.Message;
import me.itsmas.timedaccess.util.Permission;
import me.itsmas.timedaccess.util.UtilReason;
import me.itsmas.timedaccess.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
        else if (args[0].equalsIgnoreCase("remove"))
        {
            handleRemove(sender, args);
        }
        else if (args[0].equalsIgnoreCase("history"))
        {
            handleHistory(sender, args);
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

        if (args.length < 3)
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

        ArrayList<String> reason = new ArrayList<>();
        reason.addAll(Arrays.asList(args));
        reason.remove(0);
        reason.remove(0);
        reason.remove(0);

        StringBuilder reason_final = new StringBuilder();
        for (String s : reason) {
            reason_final.append(s).append(" ");
        }
        if (reason.size()==0) {
            reason_final.append("Unknown Reason.");
        }

        plugin.getDataManager().extendTime(player, time, reason_final.toString(), sender);

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

    private void handleRemove(CommandSender sender, String[] args)
    {
        if (!Permission.check(sender, "timedaccess.command.remove"))
        {
            Message.COMMAND_REMOVE_USAGE.send(sender);
            return;
        }

        if (args.length != 2)
        {
            Message.COMMAND_REMOVE_USAGE.send(sender);
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

        plugin.getDataManager().remove(player);
        Message.COMMAND_REMOVE_SUCCESS.send(sender, player.getName());
    }

    private void handleHistory(CommandSender sender, String[] args)
    {
        if (!Permission.check(sender, "timedaccess.command.history"))
        {
            Message.COMMAND_HISTORY_USAGE.send(sender);
            return;
        }

        if (args.length != 2)
        {
            Message.COMMAND_HISTORY_USAGE.send(sender);
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

        for (String s : plugin.getDataManager().getReasons(player)) {
            UtilReason reason = UtilReason.parseReason(s);
            Message.COMMAND_HISTORY_FORMAT.send(sender, new Date((long)reason.getTimestamp()).toString(), reason.getWho(), UtilTime.toHms(reason.getTimeAdded()), reason.getReason());
        }
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
