package ru.mlgtrall.jda_bot_bungee_auth.bootstrap;

import ch.jalu.injector.Injector;

/**
 * Util class for holding an injector instance.
 */

public final class InjectorContainer {

    private static Injector injector;

    public static void set(Injector injector) {
        InjectorContainer.injector = injector;
    }

    public static Injector get(){
        return injector;
    }
}
