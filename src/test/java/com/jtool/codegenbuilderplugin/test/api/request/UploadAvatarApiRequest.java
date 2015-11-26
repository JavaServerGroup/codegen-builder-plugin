package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.codegenannotation.CodeGenField;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UploadAvatarApiRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("文件的md5值")
    private String md5;

    @NotNull
    @DecimalMin("1")
    @DecimalMax("9")
    @CodeGenField("整数类型，上传资源序号标记,就是资源是这个广播的第几个资源的序号，以1为开始")
    private Integer seq;

    @NotNull
    @CodeGenField("上传文件，multipart/form-data格式上传文件")
    private MultipartFile file;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "UploadAvatarRequest{" +
                "md5='" + md5 + '\'' +
                ", seq=" + seq +
                ", file=" + file +
                '}';
    }
}
