package ru.mlgtrall.discordauth.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessagePathHelper {

    public static final String DEFAULT_LANGUAGE = CoreSettings.Messages.GLOBAL_DEFAULT_LANGUAGE.getDefaultValue();

    public static final String RESOURCE_MESSAGES_FOLDER = "messages/";

    public static final String DEFAULT_MESSAGES_FILE = createMessageFilePath(DEFAULT_LANGUAGE);

    public static final Pattern MESSAGE_FILENAME_PATTERN = Pattern.compile("messages_([a-z]+)\\.yml");

    /**
     * Creates the local path to the messages file for the provided language code.
     * @param languageCode the language code
     * @return local path to the messages file of the given language
     */
    @Contract(pure = true)
    public static @NotNull String createMessageFilePath(String languageCode){
        //TODO: throw error if language code is illegal
        return "messages/messages_" + languageCode + ".yml";
    }

    /**
     * Returns whether the given file name is a messages file.
     *
     * @param filename the file name to test
     * @return true if it is a message file, false vise versa
     */
    public static boolean isMessagesFile(String filename){
        return MESSAGE_FILENAME_PATTERN.matcher(filename).matches();
    }

    /**
     * Returns the language code the given file name is for if it is a messages file, null is returned vise versa.
     *
     * @param filename the file name to process
     * @return the language code the file name is a messages file for, or null if not applicable
     */
    public static @Nullable String getLanguageIfIsMessagesFile(String filename){
        Matcher matcher = MESSAGE_FILENAME_PATTERN.matcher(filename);
        if(matcher.matches()){
            return matcher.group(1);
        }
        return null;
    }
}
