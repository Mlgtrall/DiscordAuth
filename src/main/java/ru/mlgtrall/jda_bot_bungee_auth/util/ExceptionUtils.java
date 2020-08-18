package ru.mlgtrall.jda_bot_bungee_auth.util;

import org.jetbrains.annotations.NotNull;

public class ExceptionUtils {

    /**
     * Format the information from a Throwable as String, retaining the type and it's message.
     *
     * @param th the throwable to process
     * @return String with the type of the Throwable and it's message, e.g. "[IOException]: Could not open stream"
     */
    public static @NotNull String formatException(@NotNull Throwable th){
        return "[" + th.getClass().getSimpleName() + "]: " + th.getMessage();
    }
}
