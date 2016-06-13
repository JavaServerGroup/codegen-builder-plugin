package com.jtool.codegenbuilderplugin.model;

public class LogicInfo {
    //备注的内容
    private String info;
    //所在备注的文件名
    private String fileName;
    //所在行数
    private Integer lineNum;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    @Override
    public String toString() {
        return "LogicInfo{" +
                "info='" + info + '\'' +
                ", fileName='" + fileName + '\'' +
                ", lineNum=" + lineNum +
                '}';
    }
}
