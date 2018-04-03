package com.jtool.codegenbuilderplugin.test.api.response;

public enum RoleOperatorEnum {
    //新建工单/派发/删除、接收/编辑/分派、审核、领用材料
    CREATE("CREATE"), MANAGE("MANAGE"), CHECK("CHECK"), TAKE("TAKE");

    private RoleOperatorEnum(String type){
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
