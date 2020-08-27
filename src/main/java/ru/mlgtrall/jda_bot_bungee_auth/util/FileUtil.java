package ru.mlgtrall.jda_bot_bungee_auth.util;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public final class FileUtil {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(FileUtil.class);

    @Contract("_, _, _ -> param2")
    @SuppressWarnings("all")
    public static @NotNull File loadResource(@NotNull Plugin plugin, @NotNull File resourceFile, @NotNull String resource){
        if(!resourceFile.exists()){
            try {
                resourceFile.createNewFile();
                try(InputStream in = plugin.getResourceAsStream(resource);
                    OutputStream out = new FileOutputStream(resourceFile)){
                    ByteStreams.copy(in, out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resourceFile;
    }

    @SuppressWarnings("all")
    public static @Nullable File getResourceAsFile(@NotNull Plugin plugin, @NotNull String resource){
        File tempResourceFile = null;
        try {
            String prefix = resource.split("\\.")[0];
            String suffix = resource.split("\\.")[1];
            tempResourceFile = File.createTempFile(prefix, suffix);
            try(InputStream in = plugin.getResourceAsStream(resource);
                OutputStream out = new FileOutputStream(tempResourceFile)){
                ByteStreams.copy(in, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempResourceFile;
    }

    public static void createDirIfNotExists(@NotNull File dir){
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


    public static void createFileIfNotExists(@NotNull File file){
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

}
