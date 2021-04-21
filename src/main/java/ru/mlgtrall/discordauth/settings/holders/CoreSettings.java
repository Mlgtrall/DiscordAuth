package ru.mlgtrall.discordauth.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.log.LogLevel;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class CoreSettings implements SettingsHolder {

    private CoreSettings(){}

    @Comment({
            "If this is false, than plugin is off and it will shut down your server.",
            "You need to configure everything before setting it to true."
    })
    public static final Property<Boolean> ENABLED = newProperty("core.enabled", false);

    @Comment({
            "Plugin automatically watching files for changes if set true",
            "Default: true"
    })
    public static final Property<Boolean> WATCH_FILES = newProperty("core.watch_files", true);

    public static class Messages implements SettingsHolder{

        @Comment({
                "Default language of plugin's messages.",
                "Default: en"
        })
        public static final Property<String> GLOBAL_DEFAULT_LANGUAGE = newProperty("core.messages.default_language", "en");

        @Comment({
                "Enabling this function allows plugin to force messages for player to be sent with language that player individually use in their minecraft client.",
                "If language is not supported by plugin, then messages will be sent in a default language."
        })
        public static final Property<Boolean> ENABLE_LOCALE_LANGUAGE_RESOLVER = newProperty("core.messages.enable_locale_language", true);
    }


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

    public static class Security implements SettingsHolder{

        @Comment({
                "INFO, FINE.",
                "Default: INFO",
                "If you want a debug lvl information, you should change core.debug.enabled property to true."
        })
        public static final Property<LogLevel> GLOBAL_LOG_LEVEL = newProperty(LogLevel.class, "core.global_log_level", LogLevel.INFO);

        @Comment({
                "True or false.",
                "Default: true",
                "Writes FINE+ Debug lvl information to a log file."
        })
        public static final Property<Boolean> USE_FILE_LOGGING = newProperty("core.use_file_logging", true);


    }



}
