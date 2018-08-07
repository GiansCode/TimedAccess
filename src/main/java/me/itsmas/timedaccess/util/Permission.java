package me.itsmas.timedaccess.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Permission
{
    private Permission() {}

    public static boolean check(CommandSender sender, String permission)
    {
        if (!sender.isOp() && !sender.hasPermission(permission))
        {
            Message.NO_PERMISSION.send(sender);
            return false;
        }

        return true;
    }

    public static boolean hasBypass(Player player)
    {
        return player.isOp() || player.hasPermission("timedaccess.bypass");
    }
}
