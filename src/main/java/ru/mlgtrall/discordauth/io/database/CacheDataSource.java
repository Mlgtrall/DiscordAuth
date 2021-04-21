package ru.mlgtrall.discordauth.io.database;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheDataSource implements DataSource{

    @Getter
    private final LoadingCache<String, Optional<AuthPlayer>> cache;

    private final ListeningExecutorService executorService;

    @Getter
    private final DataSource source;

    private final ConsoleLogger log = ConsoleLoggerFactory.get(this.getClass());

    public CacheDataSource(@NotNull DataSource source){
        this.source = source;
        log.info("Loading cache data source...");

        //TODO: ??
        log.info("Registering executor service...");
        executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(
                new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("DiscordAuth-CacheLoader")
                .build()
        ));
        log.info("Done!");

        log.info("Registering cache...");
        //TODO: make configurable max size
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Optional<AuthPlayer>>(){
                    @Override
                    public Optional<AuthPlayer> load(@NotNull String key){
                        return Optional.ofNullable(source.getPlayer(key));
                    }

                    @Override
                    public ListenableFuture<Optional<AuthPlayer>> reload(@NotNull String key, @NotNull Optional<AuthPlayer> oldValue) throws Exception {
                        log.info("Updating cache...");
                        return executorService.submit(() -> load(key));
                    }
                });
        log.info("Done!");
        log.info("Cache data source loaded successfully!");
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull String name) {
        try {
            return cache.get(name).orElse(null);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public @Nullable List<AuthPlayer> getAllPlayers() {
        return source.getAllPlayers();
    }

    @Override
    public @Nullable List<AuthPlayer> getAllPlayersByIp(@NotNull String ip) {
        return source.getAllPlayersByIp(ip);
    }

    @Override
    public boolean savePlayer(@NotNull AuthPlayer player) {
        boolean result = source.savePlayer(player);
        if(result){
            //TODO: replace on LoadingCache#put?
            cache.refresh(player.getName());
        }
        return result;
    }

    @Override
    public boolean deletePlayer(@NotNull String name) {
        boolean result = source.deletePlayer(name);
        if(result){
            cache.invalidate(name);
        }
        return result;
    }

    @Override
    public boolean containsPlayer(@NotNull String playerName) {
        return source.containsPlayer(playerName);
    }

    @Override
    public boolean containsPlayer(@NotNull KeyHolder key, @NotNull AuthPlayer player) {
        return source.containsPlayer(key, player);
    }

    @Override
    public void reload() {
        source.reload();
    }

    @Override
    public void invalidateCache(String playerName) {
        cache.invalidate(playerName);
    }

    @Override
    public void refreshCache(String playerName) {
        cache.refresh(playerName);
    }
}
