package me.itsmas.timedaccess.util;

import net.md_5.bungee.api.ChatColor;

public final class Colour
{
    private Colour() {}

    public static String translate(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
