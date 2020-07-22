package ru.mlgtrall.jda_bot_bungee_auth.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class DiscordSettings implements SettingsHolder{

    private DiscordSettings(){}

//    @Comment({
//            "False by default.",
//            "Enable discord support (change to true) when you configured everything in discord section."
//    })
//    public static final Property<Boolean> ENABLED = newProperty("discord.enabled", false);

    @Comment("Command prefix to trigger the bot. By default - \"r!\", but you can change to whatever you want.")
    public static final Property<String> PREFIX = newProperty("discord.prefix", "r!");

    public static final Property<String> TOKEN = newProperty("discord.token", "-insert-your-token-here-");

    public static class Guild implements SettingsHolder {

        //TODO: make multiple guilds support?
        @Comment({
                "Your guild's id.",
                "Very important!!! Required for bot to join to your discord guild."
        })
        public static final Property<String> ID = newProperty("discord.guild.by_id", "-change-me-");

    }

    public static class Channel implements SettingsHolder{

        public static class Text implements SettingsHolder {

            public static class Listen implements SettingsHolder {

                @Comment({
                        "List of channels that bot will be listen to by their current name in discord guild.",
                        "Not recommended to use. Better to use channel's id to avoid routine if channel's name needs to be changed."
                })
                public static final Property<List<String>> BY_NAME = newListProperty("discord.role.channel.text.listen.by_name", "-some-text-channel-name-1-", "-some-text-channel-name-n-");

                @Comment("List of channels that bot will be listen to by their id in discord guild. Always better to use this, instead of names.")
                public static final Property<List<String>> BY_ID = newListProperty("discord.role.channel.text.listen.by_id", "-some-text-channel-id-1-", "-some-text-channel-id-n-");
            }

        }
    }

    public static class Role implements SettingsHolder{

        @Comment({
                "List of required roles by their current name in discord guild",
                "Not recommended to use. Better to use role's id to avoid routine if role's name needs to be changed."
        })
        public static final Property<List<String>> BY_NAME = newListProperty("discord.role.required.by_name", "-some-role-name-1-", " -some-role-name-n-" );

        @Comment("List of required roles by their id in discord guild. Always better to use this, instead of names.")
        public static final Property<List<String>> BY_ID = newListProperty("discord.role.required.by_id", "-some-role-id-1-", "-some-role-id-n-");

        @Override
        public void registerComments(@NotNull CommentsConfiguration conf) {
            conf.setComment("discord.role.required",
                    "This section is responsible for setting your guild's roles that will be required for players to join this server.");
        }
    }

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("discord", "Discord settings section.");
    }
}
