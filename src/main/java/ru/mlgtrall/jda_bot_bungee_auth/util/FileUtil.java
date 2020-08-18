package ru.mlgtrall.jda_bot_bungee_auth.util;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public final class FileUtil {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(FileUtil.class);

    /**
     * Creates the given file or throws an exception.
     *
     * @param file the file to create
     */
    public static void create(@NotNull File file) {
        try {
            boolean result = file.createNewFile();
            if (!result) {
                log.exception("Could not create file: '" + file + "'", new IllegalStateException("Could not create file: '" + file + "'") );
            }
        } catch (IOException e) {
            //TODO: ?
            throw new IllegalStateException("Error while creating file '" + file + "'", e);
        }
    }

    public static void checkOrCreateDir(@NotNull File dir){
        if(!dir.exists()){
            if(!dir.isDirectory()){
                Exception e = new IllegalArgumentException("This is not a directory");
                log.exception("Provided file is not a directory!", e);
            }
            log.info("No Data Folder found. Creating one...");
            if(dir.mkdir()){
                log.info("making Data Folder ok!");
            }
            else{
                log.info("making Data Folder caused an error...");
            }
        }
    }

    public static void checkOrCreateDirQuietly(@NotNull File dir){
        dir.mkdir();
//        try {
//            Files.createDirectory(dir.toPath(), (FileAttribute<?>) null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void checkOrCreateFile(@NotNull File file){
        if(!file.exists()){
            if(!file.isFile()){
                log.exception("Provided file was not an actual file.", new IllegalArgumentException("This is not a file."));
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
                log.exception(e.getMessage(), e);
            }
        }
    }

    public static void checkOrCreateFileQuietly(@NotNull File file){
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
