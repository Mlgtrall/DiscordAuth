package ru.mlgtrall.discordauth.annotation;

import java.lang.annotation.*;


/**
 * Annotation for specifying bungee config file through injector.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BungeeConfig {
}
