package com.example.simpleboard.enums;

public enum AdminPrivilegeEnums {
    
    PRIV_ADMIN_READ("admin privilege for read")
    , PRIV_ADMIN_WRITE("admin privilege for write")
    , PRIV_ADMIN_UPDATE("admin privilege for update")
    , PRIV_ADMIN_DELETE("admin privilege for delete")
    ;
    
    public final String descPriv;

    private AdminPrivilegeEnums(String descPriv)
    {
        this.descPriv = descPriv;
    }
}
