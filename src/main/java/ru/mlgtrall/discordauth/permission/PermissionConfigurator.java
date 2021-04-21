package ru.mlgtrall.discordauth.permission;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.node.types.PermissionNode;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class PermissionConfigurator implements Reloadable {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(PermissionConfigurator.class);

    @Inject
    private DiscordAuth pl;

    @Inject
    private TaskScheduler scheduler;

    //API instance
    @Inject
    private LuckPerms perms;

    @PostConstruct
    @Override
    public void reload() {
        log.debug("Configuring permissions...");
        addPermissionsToGroups();
    }

    private void addPermissionsToGroups(){
        GroupManager groupManager = perms.getGroupManager();
        log.debug("Adding permissions to group...");
        checkGroups(groupManager);
    }

    private void checkGroups(@NotNull GroupManager groupManager){

        scheduler.runAsync(pl, () -> {

            try{

                loadAllGroups(groupManager);

                log.debug("Loaded Groups:");
                AtomicInteger i = new AtomicInteger(1);
                Set<Group> loadedGroups = groupManager.getLoadedGroups();
                loadedGroups.forEach(it -> log.debug(i.getAndIncrement() + ". " + it.getName()));

                Stream.of(Groups.values()).forEach(group -> {
                    String name = group.getGroupName();
                    Group loadedGroup = groupManager.getGroup(name);

                    if (loadedGroup == null) {
                        log.debug("A group with name: \"" + name + "\" is not existing. Loading one...");

                        try {
                            loadedGroup = groupManager.createAndLoadGroup(name).get();
                            groupManager.saveGroup(loadedGroup).get();

                            log.debug("Group with name: \"" + loadedGroup.getName() + "\" was created and loaded.");

                        } catch (InterruptedException | ExecutionException e) {
                            log.exception(e.getMessage(), e);
                            //TODO: shut down plugin?
                        }
                    }
                });

                Stream.of(Groups.values()).forEach(group -> {
                    String name = group.getGroupName();
                    log.debug("Configuring group: \"" + name + "\"");

                    groupManager.loadGroup(name).thenAccept(permsGroup ->
                            permsGroup.ifPresent(it -> {
                                NodeMap nodes = it.data();
                                group.getPermissionNodes().forEach((key, val) -> {
                                    log.debug("Adding permission: \"" + key + "\"");
                                    nodes.add(PermissionNode.builder(key).value(val).build());
                                });
                                groupManager.saveGroup(it);
                            })
                    );
                });
            }
            catch (Exception e){
                log.exception(e.getMessage(), e);
            }
        });

    }

    private void loadAllGroups(@NotNull GroupManager groupManager) throws ExecutionException, InterruptedException {
        groupManager.loadAllGroups().get();
    }



    //TODO: refactor
    public enum Groups{
        DEFAULT("default"){{
            permissionNodes = ImmutableMap.of(
                    "discordauth.authme", true,
                    "discordauth.login", true,
                    "discordauth.reg", true
            );

        }},
        ADMIN("admin"){{
            permissionNodes = ImmutableMap.of(
                    "discordauth.admin", true,
                    "discordauth.*", true,
                    "discordauth.authme", true,
                    "discordauth.login", true,
                    "discordauth.reg", true
            );
        }};

        @Getter
        private final String groupName;

        @Getter
        protected ImmutableMap<String, Boolean> permissionNodes;

        Groups(String name){
            this.groupName = name;
        }


    }
}























