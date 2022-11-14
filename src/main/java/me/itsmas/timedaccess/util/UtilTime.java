package me.itsmas.timedaccess.util;

import java.util.concurrent.TimeUnit;

public final class UtilTime
{
    private UtilTime() {}

    public static long parseTime(String input)
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (IllegalArgumentException ex)
        {
            return -1L;
        }
    }

    public static String toHms(long millis)
    {
        if (millis == Long.MIN_VALUE)
        {
            return Message.UNLIMITED_TIME_FORMAT.format();
        }
        else if (millis <= 0L)
        {
            return Message.NO_TIME_FORMAT.format();
        }

        String formatted = "";

        long hours = TimeUnit.MILLISECONDS.toHours(millis);

        if (hours != 0)
        {
            formatted += hours + "h ";
        }

        millis -= TimeUnit.HOURS.toMillis(hours);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        if (minutes != 0)
        {
            formatted += minutes + "m ";
        }

        millis -= TimeUnit.MINUTES.toMillis(minutes);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        if (seconds != 0)
        {
            formatted += seconds + "s";
        }

        return formatted.trim();
    }
}
