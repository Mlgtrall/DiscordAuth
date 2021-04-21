package ru.mlgtrall.discordauth.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class TimeUtils {

    private TimeUtils(){}

    //TODO: make overflow-safe operations
    static public int timeToTicks(int time, @NotNull TimeUnit timeUnit){
        final int ticksPerSecond = 20;
        if(timeUnit.equals(TimeUnit.SECONDS)){
            return time * ticksPerSecond;
        }
        if(timeUnit.equals(TimeUnit.MILLISECONDS)){
            return 0;
        }
        if(timeUnit.equals(TimeUnit.MINUTES)){
            return (int) timeUnit.toSeconds(time) * ticksPerSecond;
        }
        if(timeUnit.equals(TimeUnit.HOURS)){
            return (int) timeUnit.toSeconds(time) * ticksPerSecond;
        }
        if(timeUnit.equals(TimeUnit.DAYS)){
            return (int) timeUnit.toSeconds(time) * ticksPerSecond;
        }
        return 0;
    }
}
