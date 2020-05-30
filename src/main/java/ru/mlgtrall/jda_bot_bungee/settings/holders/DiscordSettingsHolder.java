package ru.mlgtrall.jda_bot_bungee.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class DiscordSettingsHolder implements SettingsHolder{

    @Comment({
            "False by default.",
            "Enable discord support (change to true) when you configured everything in discord section."
    })
    public static final Property<Boolean> ENABLED = newProperty("discord.enabled", false);

    public static final Property<String> PREFIX = newProperty("discord.prefix", "r!");

    public static class Guild implements SettingsHolder {

        @Comment({
                "Your guild's id.",
                "Very important!!! Required for bot to join to your discord guild."
        })
        public static final Property<String> ID = newProperty("discord.guild.by_id", "-change-me-");

    }

    public static class Role implements SettingsHolder{

        @Comment("List of required roles by their current name in discord guild")
        public static final Property<List<String>> BY_NAME = newListProperty("discord.role.required.by_name", "-some-role-1-", "-some-role-2-"," -some-role-n-" );

        @Comment("")
        public static final Property<List<String>> BY_ID = newListProperty("discord.role.required.by_id", "-same-");

        @Override
        public void registerComments(@NotNull CommentsConfiguration conf) {
            conf.setComment("discord.role.required",
                    "This section is responsible for setting your guild's roles that will be required for players to join this server");
        }
    }

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("discord", "Discord settings section.");
    }
}
