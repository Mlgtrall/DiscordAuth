package ru.mlgtrall.jda_bot_bungee.bungee.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TimeManager {

    static public int timeToTicks(int time, @NotNull TimeUnit timeUnit){
        final int ticksPerSecond = 20;
        if(timeUnit.equals(TimeUnit.SECONDS)){
            return time * ticksPerSecond;
        }
        if(timeUnit.equals(TimeUnit.MILLISECONDS)){
            return 0;
        }
        if(timeUnit.equals(TimeUnit.MINUTES)){
            return time * ticksPerSecond * 60;
        }
        if(timeUnit.equals(TimeUnit.HOURS)){
            return time * ticksPerSecond * 60 * 60;
        }
        if(timeUnit.equals(TimeUnit.DAYS)){
            return time * ticksPerSecond * 60 * 60 * 24;
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
