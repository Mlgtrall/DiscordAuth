package ru.mlgtrall.jda_bot_bungee_auth.bootstrap.dependency;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.dependency.api.UndefinedAPI;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;

public final class Dependencies {

    public static final Dependencies.LiteBans LITE_BANS = new Dependencies.LiteBans();
    public static final Dependencies.LuckyPerms LUCKY_PERMS = new Dependencies.LuckyPerms();

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(Dependencies.class);

    private Dependencies(){}

    public static void checkAll(){
        LITE_BANS.reload();
        LUCKY_PERMS.reload();
    }

    public static class LiteBans extends AbstractPluginDependency<UndefinedAPI> {

        LiteBans() {
            super("LiteBans");
        }

        @Override
        public UndefinedAPI getAPI() {
            return null;
        }
    }

    public static class LuckyPerms extends AbstractPluginDependency<LuckPerms>{

        LuckyPerms() {
            super("LuckyPerms", DependencyLevel.HARD);
        }

        @Override
        public LuckPerms getAPI() {
            return LuckPermsProvider.get();
        }
    }
}
