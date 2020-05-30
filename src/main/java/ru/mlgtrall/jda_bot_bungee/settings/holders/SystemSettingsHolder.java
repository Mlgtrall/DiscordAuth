package ru.mlgtrall.jda_bot_bungee.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class SystemSettingsHolder implements SettingsHolder {


    public static class BungeeCord implements SettingsHolder{

    }

    public static class Debug implements SettingsHolder {

        @Comment({
                "This property effects on log information.",
                "False by default."
        })
        public static final Property<Boolean> ENABLED = newProperty("debug.enabled", false);

        public static class Lock implements SettingsHolder {

            public static final Property<Boolean> ENABLED = newProperty("debug.lockdown.enabled", false);

            @Comment("List of untouchable player's nicknames.")
            public static final Property<List<String>> SAINTS = newListProperty("debug.lockdown.saints", "Mlgtrall");

            @Override
            public void registerComments(@NotNull CommentsConfiguration conf) {
                conf.setComment("debug.lockdown",
                        "Locks server down for everyone, but the specified nicknames can join through authorization.",
                        "Used for debugging and tech works.");
            }
        }
    }



}
