package ru.mlgtrall.jda_bot_bungee_auth.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class CoreSettings implements SettingsHolder {

    private CoreSettings(){}

    @Comment({
            "If this is false, than plugin is off and it will shut down your server.",
            "You need to configure everything before setting it to true."
    })
    public static final Property<Boolean> ENABLED = newProperty("core.enabled", false);


    public static class BungeeCord implements SettingsHolder{

        public static class Registration implements SettingsHolder{

            @Comment("Not implemented yet.")
            public static final Property<String> LOGIN_SERVER = newProperty("core.reg.login_server", "login");

            @Comment("Not implemented yet.")
            public static final Property<String> TARGET_SERVER = newProperty("core.reg.target_server", "whitelist_mcfp");

            @Comment({
                    "Maximum number of typos that player can make during whole registration process.",
                    "This settings is helpful in order to prevent brute force attacks.",
                    "If player exceeded the max number of typos, than he will be kicked. (придумать как вписать баны сюда)"
            })
            public static final Property<String> MAX_TYPOS = newProperty("core.reg.max_typos", "20");

        }
    }

    public static class Debug implements SettingsHolder {

        @Comment({
                "This property effects on log information.",
                "False by default.",
                "Not implemented yet."
        })
        public static final Property<Boolean> ENABLED = newProperty("core.debug.enabled", false);

        public static class Lock implements SettingsHolder {

            @Comment({"If this is true, than servers will be restricted for everyone, except saints."})
            public static final Property<Boolean> ENABLED = newProperty("core.debug.lock.enabled", false);

            @Comment("List of untouchable player's nicknames.")
            public static final Property<List<String>> SAINTS = newListProperty("core.debug.lock.saints", "Mlgtrall");

            @Override
            public void registerComments(@NotNull CommentsConfiguration conf) {
                conf.setComment("core.lock",
                        "Locks server down for everyone, but the specified nicknames can join through authorization.",
                        "Used for debugging and tech works.");
            }
        }
    }



}
