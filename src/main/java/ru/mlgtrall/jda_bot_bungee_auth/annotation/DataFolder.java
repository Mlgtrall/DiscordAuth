package ru.mlgtrall.jda_bot_bungee_auth.annotation;

import java.lang.annotation.*;

/**
 * Annotation for specifying the plugin's data folder through injector.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFolder {
}
