package ru.mlgtrall.discordauth.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

//TODO: Make Iterable
public class DataSourceSettings implements SettingsHolder {

    private DataSourceSettings(){}

    @Comment({
            "Database backend.",
            "Available sources: mysql "
    })
    public static final Property<String> BACKEND = newProperty("data_source.backend", "mysql");

    @Comment({
            "You want to use it to improve data retrieving speed from database in general.",
            "Default: true"
    })
    public static final Property<Boolean> USE_CACHING = newProperty("data_source.use_caching", true);

    public static class MySQL implements SettingsHolder{

        @Comment({"Connection parameters."})
        public static final Property<String > HOST = newProperty("data_source.mysql.host", "127.0.0.1");

        public static final Property<Integer> PORT = newProperty("data_source.mysql.port", 3306);

        public static final Property<String> USER = newProperty("data_source.mysql.name", "-change-me-");

        public static final Property<String> PASSWORD = newProperty("data_source.mysql.password", "-change-me-");

        @Comment("Change to what ever you want (preferred name: your server's name)")
        public static final Property<String> DATABASE_NAME = newProperty("data_source.mysql.database", "my_minecraft_server");

        public static final Property<String> TABLE = newProperty("data_source.mysql.table_name", "main");

    }

    @Deprecated
    public static class YML implements SettingsHolder {
        public static final Property<String> FILE = newProperty("data_source.yml.file", "players.yml");

        @Override
        public void registerComments(CommentsConfiguration conf) {
            conf.setComment("data_source.yml", "Deprecated. Changes won't affect anything.");
        }
    }

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("data_source", "Database settings.");
    }


}
