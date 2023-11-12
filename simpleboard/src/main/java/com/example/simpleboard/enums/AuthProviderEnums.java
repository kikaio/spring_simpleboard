package com.example.simpleboard.enums;

public enum AuthProviderEnums {

    PROVIDER_EMAIL("email", "provider with just email")
    , PROVIDER_GOOGLE("google", "provider google oauth2")
    , PROVIDER_GIT("git", "provider git oauth2, not support yet")
    ;

    public final String registName;
    public final String descProvider;

    private AuthProviderEnums(String registName, String descProvider)
    {
        this.registName = registName;
        this.descProvider = descProvider;
    }
}
