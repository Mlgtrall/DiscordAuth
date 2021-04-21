package ru.mlgtrall.discordauth.annotation;

import java.lang.annotation.*;

/**
 * Annotation for specifying the plugin's data folder through injector.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFolder {
}
