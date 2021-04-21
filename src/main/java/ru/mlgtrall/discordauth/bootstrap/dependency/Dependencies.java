package ru.mlgtrall.discordauth.bootstrap.dependency;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.bootstrap.dependency.api.UndefinedAPI;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;

public final class Dependencies {

    public static final AbstractPluginDependency<UndefinedAPI> LITE_BANS = new AbstractPluginDependency<UndefinedAPI>("LiteBans") {
        @Contract(pure = true)
        @Override
        public @Nullable UndefinedAPI getAPI() {
            return null;
        }
    };
    public static final AbstractPluginDependency<LuckPerms> LUCKY_PERMS = new AbstractPluginDependency<LuckPerms>("LuckPerms", DependencyLevel.HARD){

        @Override
        public @NotNull LuckPerms getAPI() {
            return LuckPermsProvider.get();
        }
    };

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(Dependencies.class);

    private Dependencies(){}

    public static void checkAll(){
        LITE_BANS.reload();
        LUCKY_PERMS.reload();
    }

}
