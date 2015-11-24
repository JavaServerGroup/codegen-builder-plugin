package com.jtool.codegenbuilderplugin.model;

public class LogicInfo {
    private String info;
    private String fileName;
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
