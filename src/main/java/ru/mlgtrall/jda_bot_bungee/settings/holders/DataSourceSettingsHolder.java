package ru.mlgtrall.jda_bot_bungee.settings.holders;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class DataSourceSettingsHolder implements SettingsHolder {

    @Comment("Database backend (yml, mysql).")
    public static final Property<String> BACKEND = newProperty("data_source.backend", "mysql");

    public static class MySQL implements SettingsHolder{
        @Comment("Connection parameters.")
        public static final Property<String > HOST = newProperty("data_source.mysql.host", "127.0.0.1");

        public static final Property<Integer> PORT = newProperty("data_source.mysql.port", 3306);

        public static final Property<String> USER = newProperty("data_source.mysql.name", "-change-me-");

        public static final Property<String> PASSWORD = newProperty("data_source.mysql.password", "-change-me-");

        public static final Property<String> DATABASE = newProperty("data_source.mysql.database", "my_minecraft_server");

        public static final Property<String> TABLE = newProperty("data_source.mysql.table_name", "main");

    }

    public static class YML implements SettingsHolder {
        public static final Property<String> FILE = newProperty("data_source.yml.file", "players.yml");
    }

    @Override
    public void registerComments(@NotNull CommentsConfiguration conf) {
        conf.setComment("data_source", "Database settings.");
    }


}
