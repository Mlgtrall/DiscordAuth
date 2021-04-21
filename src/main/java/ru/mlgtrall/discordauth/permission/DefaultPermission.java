package ru.mlgtrall.discordauth.permission;

import net.md_5.bungee.api.CommandSender;

public enum DefaultPermission {
    /**
     * No one has permission.
     */
    NOT_ALLOWED{
        @Override
        public boolean evaluate(CommandSender sender) {
            return false;
        }
    },

//    OP_ONLY{
//        @Override
//        public boolean evaluate(CommandSender sender) {
//            return sender instanceof ProxiedPlayer && ((ProxiedPlayer) sender) ;
//        }
//    }
    ;

    /**
     * Evaluates whether permission is granted to the sender or not.
     * @param sender the sender to process
     * @return true if the sender has permission, false otherwise.
     */
    public abstract boolean evaluate(CommandSender sender);
}
