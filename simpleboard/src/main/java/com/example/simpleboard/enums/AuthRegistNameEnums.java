package com.example.simpleboard.enums;

public enum AuthRegistNameEnums {
    
    DIRECT_EMAIL("directE-email"),
    GOOGLE("google"),
    GIT("git"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String registedName;

    private AuthRegistNameEnums(String registedName)
    {
        this.registedName = registedName;
    }

    public String getRegistrationName()
    {
        return registedName;
    }
}
