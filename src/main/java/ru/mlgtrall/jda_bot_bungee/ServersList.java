package ru.mlgtrall.jda_bot_bungee;

public enum ServersList {
    MAIN("whitelist_mcfp"), LOGIN("login");
    private final String name;

    public static boolean isAuthorizedServer(String name){
        for (ServersList server : values()) {
            if(server.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return this.name;
    }
    ServersList(String name){
        this.name = name;
    }
}
