package ru.mlgtrall.discordauth.io.log;


import com.google.common.base.Throwables;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;
import ru.mlgtrall.discordauth.util.ExceptionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: a normal logging system (sl4j?) with log file output
//decorator for jul
public class ConsoleLogger{

    /**
     * System's architecture based new line separator code.
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /** Formatter which formats dates to something like "[08-16 21:18:46]" for any given LocalDateTime. */
    private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendLiteral('[')
            .appendPattern("MM-dd HH:mm:ss")
            .appendLiteral(']')
            .toFormatter();

    // Outside references
    private static File logFile;
    private static Logger logger;

    // Shared state
    private static OutputStreamWriter fileWriter;

    // Individual state
    @Getter
    private final String name;
    @Getter
    private LogLevel logLevel = LogLevel.INFO;

    public static File getLogFile() {
        return logFile;
    }

    public static Logger getLogger() {
        return logger;
    }

    public ConsoleLogger(String name){
        this.name = name;
    }

    public static void init(Logger logger, File logFile){
        ConsoleLogger.logger = logger;
        ConsoleLogger.logFile = logFile;
    }

    public static void initSharedSettings(@NotNull Settings settings){
        boolean useLogging = settings.getProperty(CoreSettings.Security.USE_FILE_LOGGING);
        if(useLogging){
            initFileWriter();
        } else{
            closeFileWriter();
        }
    }

    public void initSettings(@NotNull Settings settings){
        if(settings.getProperty(CoreSettings.Debug.ENABLED)){
            this.logLevel = LogLevel.DEBUG;
        }else {
            this.logLevel = settings.getProperty(CoreSettings.Security.GLOBAL_LOG_LEVEL);
        }

    }

    public static void closeFileWriter(){
        if(fileWriter != null){
            try {
                fileWriter.flush();
            } catch (IOException ignored) {
            } finally {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Failed to close resource", e);
                }
            }
        }
    }

    public void warning(String message){
        logger.warning(message);
        writeLog("[WARN] " + message);
    }

    public void exception(String message, Throwable th){
        warning(message + " " + ExceptionUtils.formatException(th));
        writeLog(Throwables.getStackTraceAsString(th));
    }

    public void exception(Throwable th){
        warning(ExceptionUtils.formatException(th));
        writeLog(Throwables.getStackTraceAsString(th));
    }

    public void info(String message){
        logger.info(message);
        writeLog("[INFO] " + message);
    }

    public void fine(String message){
        if(logLevel.includes(LogLevel.FINE)){
            logger.info(message);
            writeLog("[FINE] " + message);
        }
    }

    public void debug(String message) {
        if(logLevel.includes(LogLevel.DEBUG)) {
            logAndWriteWithDebugPrefix(message);
        }
    }

    public void fatal(String message, Throwable th){
        exception("[FATAL ERROR] " + message, th);
        throw new Error(th);
    }

    private void logAndWriteWithDebugPrefix(String message){
        String debugMessage = "[DEBUG] " + message;
        logger.info(debugMessage);
        writeLog(debugMessage);
    }

    private static void writeLog(String message){
        if(fileWriter != null){
            String dateTime = DATE_FORMAT.format(LocalDateTime.now());
            try{
                fileWriter.write(dateTime);
                fileWriter.write(": ");
                fileWriter.write(message);
                fileWriter.write(NEW_LINE);
                fileWriter.flush();

            }catch (IOException ignored){}
        }
    }

    private static void initFileWriter() {
        if(fileWriter == null){
            try(FileOutputStream fos = new FileOutputStream(logFile, true)){
                fileWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            }
            catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to created file writer to log file", e);
            }
        }
    }


}
