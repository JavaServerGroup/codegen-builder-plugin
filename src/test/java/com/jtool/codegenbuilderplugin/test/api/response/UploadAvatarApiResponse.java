package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class UploadAvatarApiResponse {

    @NotNull
    @CodeGenField("状态码, 0：完成")
    private String code;

    @CodeGenField("文件base64")
    private String fileContent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "UploadAvatarResponse{" +
                "code='" + code + '\'' +
                ", fileContent='" + fileContent + '\'' +
                '}';
    }
}
