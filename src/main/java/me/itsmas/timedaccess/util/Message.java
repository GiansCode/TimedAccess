package me.itsmas.timedaccess.util;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.text.MessageFormat;
import java.util.List;

import static me.itsmas.timedaccess.util.Colour.translate;

public enum Message
{
    NO_PERMISSION,
    COMMAND_USAGE,
    COMMAND_GIVE_USAGE,
    COMMAND_GET_USAGE,
    COMMAND_GIVE_SUCCESS,
    COMMAND_GET_SUCCESS,
    COMMAND_GET_RECEIVED,
    PLAYER_OFFLINE,
    INVALID_TIME,
    KICK_NO_TIME,
    UNLIMITED_TIME_FORMAT,
    NO_TIME_FORMAT,
    ;

    private String value = name();

    public void send(CommandSender sender, Object... args)
    {
        if (value != null)
        {
            sender.sendMessage(format(args));
        }
    }

    public String format(Object... args)
    {
        return MessageFormat.format(value, args);
    }

    @SuppressWarnings("unchecked")
    public static void init(Plugin plugin)
    {
        for (Message message : values())
        {
            Object object = plugin.getConfig().get("messages." + message.name().toLowerCase());

            if (object == null)
            {
                plugin.getLogger().severe("Value missing for message " + message.name());
                continue;
            }

            String value =
                object instanceof String ? (String) object :
                    object instanceof List ? String.join("\n", (List<String>) object) :
                        null;

            if (value == null)
            {
                plugin.getLogger().severe("Invalid data type for message " + message.name());
                continue;
            }

            message.value = translate(value);
        }
    }
}
