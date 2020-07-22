package ru.mlgtrall.jda_bot_bungee_auth.bungee.listener;

import lombok.NonNull;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.Servers;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.connection.Connection;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthedPlayers;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginSession;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginSessionPool;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginState;
import ru.mlgtrall.jda_bot_bungee_auth.data.state.SendToAddress;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBotService;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.settings.holders.CoreSettings;
import ru.mlgtrall.jda_bot_bungee_auth.util.BungeeChatConfig;
import ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil;
import ru.mlgtrall.jda_bot_bungee_auth.util.TitleManager;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil.socketAddressToIp;

public class PlayerListener implements Listener {

    @Inject
    private Main pl;

    @Inject
    private Logger log;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private DataSource db;

    @Inject
    private LoginSessionPool sessionPool;

    @Inject
    private Settings settings;

    @Inject
    private DiscordBotService botService;

    @Inject
    private AuthedPlayers verify;

    public PlayerListener(){
    }

    @EventHandler
    public void onPreLogin(@NotNull final PreLoginEvent e){
        log.info("Someone is joining!");
        log.info("PreLoginEvent called!");

        PendingConnection conn = e.getConnection();

        SocketAddress address = conn.getSocketAddress();

        LoginSession session = sessionPool.initSession(address).playerDefiner().byIp(address).tryDefine();
        AuthPlayer authPlayer = session.getAuthPlayer();

        log.info("Session restored from SocketAddress " + address.toString() + " is " + session.toString());

        //If authPlayer == null => no authPlayer in db. TODO: make configurable if i want to kick players not in db?
        if(authPlayer == null){
            return;
        }

        log.info("AuthPlayer from session = " + authPlayer.toString());

        @NonNull
        LoginState state = session.getState();
        SendToAddress to = state.getSendTo();

        //TODO: make reasons to kick from there?
        if(to.equals(SendToAddress.KICK)) {
            log.info("SendToAddress = Kick. Cancelling login.");
            e.setCancelReason(state.getKickReason());
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onLogin(@NotNull final LoginEvent e){
        //log.info("LoginEvent called!");
    }

    @EventHandler
    public void onPostLogin(@NotNull final PostLoginEvent e){
        log.info("PostLoginEvent called!");
        ProxiedPlayer proxied = e.getPlayer();
        SocketAddress addr = proxied.getSocketAddress();
        String playerName = proxied.getName();

        //Allow to pass only saints if bungee is under lock
        if(settings.getProperty(CoreSettings.Debug.Lock.ENABLED)){
            List<String> saints = settings.getProperty(CoreSettings.Debug.Lock.SAINTS);
            try {
                if (!saints.contains(playerName)) return;
            }catch (NullPointerException ex){ ex.printStackTrace();}
        }

        LoginSession session = sessionPool.getSession(addr).playerDefiner().byName(playerName).tryDefine();
        LoginState state = session.getState();
        AuthPlayer auth = session.getAuthPlayer();

        log.info("Session restored from SocketAddress " + addr.toString() + " is " + session.toString());

        if(auth == null) return;

        log.info("AuthPlayer from session = " + auth.toString());


        if(state.getSendTo().equals(SendToAddress.KICK))
            return;

        //Cancel deleting from db that was scheduled in discord auth command.
        HashMap<String, Integer> nameTaskIdMap = pl.getNameTaskIdMap();
        if(nameTaskIdMap.containsKey(playerName)) {
            int taskid = nameTaskIdMap.get(playerName);
            pl.getProxy().getScheduler().cancel(taskid);
            nameTaskIdMap.remove(playerName);
        }

        String userId = auth.getDiscordID();
        //TODO: move? process this better
        if(Optional.ofNullable(userId).orElse("null").equals("null")) {
            return;
        }

        Guild guild = botService.getBot().getRelevantGuild();
        Member member = guild.getMemberById(userId);

        if(member == null){
            session.setState(LoginState.NOT_IN_GUILD);
            return;
        }

        boolean hasRole = botService.hasRequiredRole(member);

        if(!hasRole){
            session.setState(LoginState.NO_ROLE);
            return;
        }

        //If all ok -> verify
        if(state.equals(LoginState.VERIFIED)){
            verify.add(auth);
            auth.setLastIP(socketAddressToIp(proxied.getSocketAddress()));
            auth.setLastDate(new Date().toString());
            scheduler.runAsync(pl, () -> db.savePlayer(auth));
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(@NotNull final ServerConnectEvent e){
        log.info("ServerConnectEvent called!");

        ServerInfo target = e.getTarget();
        ProxiedPlayer p = e.getPlayer();
        log.info("Target of player " + p.toString() + " is " + target.toString());
        SocketAddress addr = p.getSocketAddress();

        LoginSession session = sessionPool.getSession(addr);
        LoginState state = session.getState();
        AuthPlayer authPlayer = session.getAuthPlayer();

        log.info("Session restored from SocketAddress " + addr.toString() + " is " + session.toString());

        if(authPlayer == null){
            log.info("Player " + p.toString() + " not in db. Cancelling login.");
            session.setState(LoginState.PLAYER_UNKNOWN);
            return;
        }

        log.info("AuthPlayer from session = " + authPlayer.toString());

        SendToAddress to = state.getSendTo();
        if (!to.equals(SendToAddress.KICK)) {
            log.info("Player was marked to send to server because of it's login state \"" + to.getTarget().getName() + "\"");
            //Changing target to required that was restored from db player
            e.setTarget(to.getTarget().getInfo());
        }else{
            log.info("Player was marked to kick sending to login than kick.");
            e.setTarget(SendToAddress.LOGIN_SERVER.getTarget().getInfo());
        }

    }

    @EventHandler
    public void onServerConnected(@NotNull final ServerConnectedEvent e){
        log.info("ServerConnectedEvent called!");

        Server server = e.getServer();

        if(!Servers.isAuthorizedServer(server)) return;


        ProxiedPlayer p = e.getPlayer();
        SocketAddress addr = p.getSocketAddress();

        LoginSession session = sessionPool.getSession(addr);
        LoginState state = session.getState();

        String name = p.getName();

        if(state.getSendTo().equals(SendToAddress.KICK)){
            log.info("Player was marked to kick. Kicking player " + p.toString());
            Connection.kick(p, state.getKickReason());
            return;
        }

        UUID uuid = p.getUniqueId();
        Map<String, ServerInfo> servers = pl.getProxy().getServers();

        try {
            scheduler.schedule(pl, () -> {

                if (!verify.isAuthed(name) && !servers.get(Servers.LOGIN.getName()).getPlayers().contains(p)) {
                    pl.getLogger().info("AuthPlayer is not in whitelist but on a Main server!");
                    Connection.tryConnect(p, Servers.LOGIN.getName());
                    return;
                }

                if (verify.isAuthed(name) && servers.get(Servers.LOGIN.getName()).getPlayers().contains(p)) {
                    pl.getLogger().info("AuthPlayer is in whitelist, but in login server!");
                    Connection.tryConnect(p);
                    return;
                }

                if (servers.get(Servers.LOGIN.getName()).getPlayers().contains(p)) {
                    //Login timeout
                    scheduler.schedule(pl, () -> {
                        try {
                            if (pl.getProxy().getPlayer(uuid).isConnected() && !verify.isAuthed(name)) {
                                Connection.kick(p, BungeeChatConfig.fromConfig("timeout"));
                            }
                        } catch (NullPointerException ignored) { } }, 3, TimeUnit.MINUTES);

                    if (state.equals(LoginState.LOGIN)) {
                        scheduler.schedule(pl, () -> TitleManager.send(p, BungeeChatConfig.fromConfigRaw("title_login")), 2, TimeUnit.SECONDS);
                        p.sendMessage(BungeeChatConfig.fromConfig("join3"));

                    } else if (state.equals(LoginState.REGISTRATION)) {
                        scheduler.schedule(pl, () -> TitleManager.send(p, BungeeChatConfig.fromConfigRaw("title_reg")), 2, TimeUnit.SECONDS);
                        p.sendMessage(BungeeChatConfig.fromConfig("join_2"));

                    } else if (state.equals(LoginState.DISCORD_SYNCHRONIZATION)) {
                        scheduler.schedule(pl, () -> TitleManager.send(p, BungeeChatConfig.fromConfigRaw("title_auth")), 2, TimeUnit.SECONDS);
                        p.sendMessage(BungeeChatConfig.fromConfig("join"));
                    }
                }
                if (state.equals(LoginState.VERIFIED)) {
                    scheduler.schedule(pl, () -> TitleManager.send(p, BungeeChatConfig.fromConfigRaw("title_welcome")), 2, TimeUnit.SECONDS);
                }

            }, 1, TimeUnit.SECONDS);
        }catch (NullPointerException ignored) { }
        finally {
            sessionPool.close(addr);
        }
    }

    @EventHandler
    public void onServerSwitch(@NotNull final ServerSwitchEvent e){
        pl.getLogger().info("ServerSwitchEvent called!");
    }

    @EventHandler
    public void onServerDisconnect(@NotNull final ServerDisconnectEvent e){
        log.info("ServerDisconnectEvent called!");
    }

    @EventHandler
    public void onPlayerDisconnect(@NotNull final PlayerDisconnectEvent e){
        log.info("PlayerDisconnectEvent called!");
        ProxiedPlayer p = e.getPlayer();
        String name = p.getName();

        SocketAddress addr = p.getSocketAddress();

        verify.remove(name);
        sessionPool.close(addr);
    }


    private final Map<UUID, Integer> playerTries = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(final ChatEvent e){
        if(!(e.getSender() instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        String name = player.getName();

        if(verify.isAuthed(name)) return;

        if(!player.getServer().getInfo().getName().equals(Servers.LOGIN.getName())) return;

        UUID uuid = player.getUniqueId();
        String message = e.getMessage();

        if(!playerTries.containsKey(uuid))
            playerTries.put(uuid, 0);

        if(!e.isCommand() && !e.isProxyCommand()){
            player.sendMessage(BungeeChatConfig.fromConfig("not_command", true));
            e.setCancelled(true);
            triesCounter(player);
            return;
        }

        //TODO: make some method that can return boolean if it is one of this plugin's command
        if(message.contains("/authme") || message.contains("/reg") || message.contains("/login") || message.contains("/r") || message.contains("/l")){
            triesCounter(player);
            return;
        }else{
            player.sendMessage(BungeeChatConfig.fromConfig("not_command",true));
            e.setCancelled(true);
            triesCounter(player);
        }

    }

    //TODO: move to some class?
    //TODO: make max count configurable
    private void triesCounter(@NotNull ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        int count = playerTries.get(uuid);
        ++count;
        if(count >= 10){
            Connection.kick(player, BungeeChatConfig.fromConfig("disconnect"));
            playerTries.remove(uuid);
        }else{
            playerTries.put(uuid, count);
        }
    }

}
