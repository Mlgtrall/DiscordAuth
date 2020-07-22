package ru.mlgtrall.jda_bot_bungee_auth.util;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class FileUtil {

    private static final Logger log = Main.getInstance().getLogger();

    /**
     * Creates the given file or throws an exception.
     *
     * @param file the file to create
     */
    public static void create(@NotNull File file) {
        try {
            boolean result = file.createNewFile();
            if (!result) {
                throw new IllegalStateException("Could not create file '" + file + "'");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error while creating file '" + file + "'", e);
        }
    }

    public static void checkOrCreateDir(@NotNull File file){
        if(!file.exists()){
            if(!file.isDirectory()){
                throw new IllegalArgumentException("This is not a directory");
            }
            log.info("No Data Folder found... Creating one...");
            if(file.mkdir()){
                log.info("making Data Folder ok!");
            }
            else{
                log.info("making Data Folder caused an error...");
            }
        }
    }

    public static void checkOrCreateFile(@NotNull File file){
        if(!file.exists()){
            if(!file.isFile()){
                throw new IllegalArgumentException("This is not a file.");
            }
            log.info("No Data Folder found... Creating one...");
            try {
                if(file.createNewFile()){
                    log.info("making Data Folder ok!");
                }
                else{
                    log.info("making Data Folder caused an error...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
