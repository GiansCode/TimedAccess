package me.itsmas.timedaccess.placeholder;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.UtilTime;
import org.bukkit.entity.Player;

public class PlaceholderHook extends EZPlaceholderHook
{
    private final TimedAccess plugin;

    public PlaceholderHook(TimedAccess plugin)
    {
        super(plugin, "timedaccess");

        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String tag)
    {
        if (player == null)
        {
            return null;
        }

        if (tag.equalsIgnoreCase("time"))
        {
            return UtilTime.toHms(plugin.getDataManager().getTimeRemaining(player));
        }

        return null;
    }
}
