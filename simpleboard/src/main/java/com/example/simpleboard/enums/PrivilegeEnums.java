package com.example.simpleboard.enums;

public enum PrivilegeEnums {
    
    PRIV_WRITE("privilege for post write")
    , PRIV_READ("privilege for post read")
    , PRIV_UPDATE("privilege for post update")
    , PRIV_DELETE("privilege for post delete")
    ;

    public final String descPriv;

    private PrivilegeEnums(String descPriv)
    {
        this.descPriv = descPriv;
    }
}
