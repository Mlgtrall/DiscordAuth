package ru.mlgtrall.discordauth.util;

import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.exception.NotExistingResourceException;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;

import java.io.*;

public final class FileUtils {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(FileUtils.class);

    /**
     * Loads all up-to-date info from jar's  to it's copy in outside of jar.
     *
     * @param whereToLoad file outside jar
     * @param localResourcePath local resource path within jar
     * @return An updated file
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "UnstableApiUsage"})
    public static @NotNull File loadResourceFromJar(@NotNull File whereToLoad, @NotNull String localResourcePath) throws NotExistingResourceException {
        if(!whereToLoad.exists()){
            try {
                whereToLoad.createNewFile();
                try(InputStream in = DiscordAuth.class.getClassLoader().getResourceAsStream(localResourcePath);
                    OutputStream out = new FileOutputStream(whereToLoad)){
                    if (in != null) {
                        ByteStreams.copy(in, out);
                    }else{
                        throw new NotExistingResourceException("Could not load resource.");
                    }
                }
            } catch (IOException | NotExistingResourceException e) {
                log.exception(e);
                if(e instanceof NotExistingResourceException){
                    throw (NotExistingResourceException) e;
                }
            }
        }
        return whereToLoad;
    }

    /**
     *
     * @param localResourcePath local resource path name within jar
     * @return localResourcePath as file
     */
    @SuppressWarnings({"UnstableApiUsage", "ResultOfMethodCallIgnored"})
    public static @Nullable File getResourceAsFileFromJar(@NotNull String localResourcePath){
        File tempResourceFile = null;
        normalizeResourcePath(localResourcePath);
        try {
            String prefix = localResourcePath.split("\\.")[0];
            String suffix = localResourcePath.split("\\.")[1];
            tempResourceFile = File.createTempFile(prefix, suffix);
            try(InputStream in = DiscordAuth.class.getClassLoader().getResourceAsStream(localResourcePath);
                OutputStream out = new FileOutputStream(tempResourceFile)){
                if (in != null){
                    ByteStreams.copy(in, out);
                }
            }
        } catch (IOException e) {
            log.exception(e);
        }
        return tempResourceFile;
    }

    /**
     * Returns a JAR file as stream. Returns null if it doesn't exist
     * @param path the local path (starting from resources folder, e.g. "config.yml" for '.../resources/config.yml')
     * @return the stream if the file exists, or null vice versa
     */
    public static InputStream getResourceFromJar(@NotNull String path){
        // ClassLoader#getResourceAsStream does not deal with the '\' path separator: replace with '/'
        final String normalizedPath = normalizeResourcePath(path);
        return DiscordAuth.class.getClassLoader().getResourceAsStream(normalizedPath);
    }

    @Contract(pure = true)
    public static @NotNull String normalizeResourcePath(@NotNull String path){
        return path.replace("\\","/");
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
