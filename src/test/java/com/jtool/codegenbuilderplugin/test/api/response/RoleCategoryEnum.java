package com.jtool.codegenbuilderplugin.test.api.response;

public enum RoleCategoryEnum {
    SUPER("SUPER"), MANAGER("MANAGER"), TEAM("TEAM");

    private RoleCategoryEnum(String type){
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
