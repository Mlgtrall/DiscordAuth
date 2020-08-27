package ru.mlgtrall.jda_bot_bungee_auth.bootstrap.dependency;

import lombok.Getter;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.Reloadable;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;

public abstract class AbstractPluginDependency<API> implements Reloadable {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(AbstractPluginDependency.class);

    private static final Main pl = Main.getInstance();

    @Getter
    private final String pluginName;

    @Getter
    private final DependencyLevel lvl;

    public AbstractPluginDependency(String pluginName){
        this(pluginName, DependencyLevel.SOFT);
    }

    public AbstractPluginDependency(String pluginName, DependencyLevel lvl){
        this.pluginName = pluginName;
        this.lvl = lvl;
    }

    @Override
    public void reload(){
        log.fine("Checking plugin dependency: \"" + pluginName + "\"...");
        if(pl.getProxy().getPluginManager().getPlugin(pluginName) == null){
            if(lvl.equals(DependencyLevel.SOFT)){
                log.warning("\"" +pluginName + "\" plugin was not found.");
            }else{
                log.fatal("\"" +pluginName + "\" plugin was not found.\"\n" +
                        "Shutting down plugin due to it's hard dependency to this missing plugin.",
                        new IllegalStateException("Hard depended dependency was not found!"));
            }
        }else{
            log.fine("Dependency plugin \"" + pluginName + "\" was found! Ok!");
        }
    }

    public abstract API getAPI();
}
