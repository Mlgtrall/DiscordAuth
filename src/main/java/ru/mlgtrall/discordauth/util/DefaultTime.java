package ru.mlgtrall.discordauth.util;

import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Represents a LocalDateTime wrapper in default zone "Europe/Moscow"
 */
public final class DefaultTime {

    public static final String DEFAULT_ZONE = "Europe/Moscow";
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_ZONE);

    public static final Clock CLOCK = Clock.system(DEFAULT_ZONE_ID);

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE_ID));
    }

    public static @NotNull Instant instant() {return CLOCK.instant();}

    public static @NotNull LocalDateTime now(){
        return LocalDateTime.now(CLOCK);
    }

    public static @NotNull ZonedDateTime zoned(){return ZonedDateTime.now(CLOCK);}

    public static @NotNull ZonedDateTime zoned(LocalDateTime ldt){return ZonedDateTime.of(ldt, DEFAULT_ZONE_ID);}


}
