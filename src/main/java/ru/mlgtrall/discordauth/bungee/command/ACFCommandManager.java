package ru.mlgtrall.discordauth.bungee.command;

import co.aikar.commands.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.data.AuthedPlayers;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.database.keys.DataColumns;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ACFCommandManager implements Reloadable {

    @Inject
    private ProxyServer proxy;

    @Inject
    private BungeeCommandManager acfManager;

    @Inject
    private DataSource db;

    @Inject
    private AuthedPlayers verify;


    @PostConstruct
    @Override
    public void reload() {
        registerCommandCompletions();
        registerCommandContexts();
        registerAllCommands();
    }

    /**
     * Async completions - only for MC Paper!
     */
    private void registerCommandCompletions(){

        final CommandCompletions<BungeeCommandCompletionContext> completions = acfManager.getCommandCompletions();

        completions.registerAsyncCompletion("datacolumns", handler ->
                Stream.of(DataColumns.values())
                        .map(it -> it.getContextualKey().toString())
                        .collect(Collectors.toSet())
        );

        completions.registerAsyncCompletion("allplayers", handler -> {
            final Set<String> result = new HashSet<>();

            Optional<Collection<AuthPlayer>> authedPlayers = Optional.ofNullable(verify.getCache()).map(Map::values);
            authedPlayers.ifPresent( c ->
                    c.stream()
                            .filter(Objects::nonNull)
                            .map(AuthPlayer::getName)
                            .peek(result::add));

            Optional<Collection<AuthPlayer>> dbplayers = Optional.ofNullable(db.getAllPlayers());
            dbplayers.ifPresent( c ->
                    c.stream()
                            .filter(Objects::nonNull)
                            .map(AuthPlayer::getName)
                            .peek(result::add));

            return result;
        }
        );

        completions.registerAsyncCompletion("dbplayers", handler ->
                Objects.requireNonNull(db.getAllPlayers())
                        .stream()
                        .filter(Objects::nonNull)
                        .map(AuthPlayer::getName)
                        .collect(Collectors.toSet())
        );

        completions.registerAsyncCompletion("authedplayers", handler ->
                verify.getCache()
                        .values()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(AuthPlayer::getName)
                        .collect(Collectors.toSet())
        );
    }

    private void registerCommandContexts(){

        final CommandContexts<BungeeCommandExecutionContext> contexts = acfManager.getCommandContexts();

        contexts.registerContext(AuthPlayer.class, c -> {
            CommandSender sender = c.getSender();

            if(!(sender instanceof ProxiedPlayer)){
                return null;
            }
            ProxiedPlayer player = (ProxiedPlayer) sender;
            String name = player.getName();
            return Optional.ofNullable(verify.getAuthPlayer(name)).orElse(db.getPlayer(name));
        });
    }

    private void registerAllCommands(){
        final PluginManager pluginManager = proxy.getPluginManager();



    }

}
