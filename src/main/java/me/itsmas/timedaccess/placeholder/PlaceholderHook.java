package me.itsmas.timedaccess.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.itsmas.timedaccess.TimedAccess;
import me.itsmas.timedaccess.util.UtilTime;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderHook extends PlaceholderExpansion
{
    private final TimedAccess plugin;

    public PlaceholderHook(TimedAccess plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "Gian_MrButtersDev";
    }

    @Override
    public String getIdentifier() {
        return "TimedAccess";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("time"))
        {
            return UtilTime.toHms(plugin.getDataManager().getTimeRemaining(player));
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
