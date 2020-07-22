package ru.mlgtrall.jda_bot_bungee_auth.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class TimeUtil {

    private TimeUtil(){}

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
        if(timeUnit.equals(TimeUnit.MICROSECONDS)){
            return 0;
        }
        if(timeUnit.equals(TimeUnit.NANOSECONDS)){
            return 0;
        }
        return 0;
    }
}
