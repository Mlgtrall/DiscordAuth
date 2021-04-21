package ru.mlgtrall.discordauth.security;

public abstract class StringContract {

    protected static final String ALLOWED_REGEX = "[a-zA-Z0-9_]+";

    protected static final String ALLOWED_SYMBOLS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";

    protected StringContract(){ }

}
