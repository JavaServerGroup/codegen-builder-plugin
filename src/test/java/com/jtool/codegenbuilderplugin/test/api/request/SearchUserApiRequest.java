package com.jtool.codegenbuilderplugin.test.api.request;

import com.jtool.annotation.AvailableValues;
import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.*;

public class SearchUserApiRequest {

    @NotNull
    @Size(min = 1, max = 20)
    @CodeGenField("用户所在国家")
    private String country;

    @NotNull
    @Min(0)
    @Max(120)
    @Digits(integer = 3, fraction = 0)
    @CodeGenField("年龄")
    private Integer age;

    @NotNull
    @Min(0)
    @Max(250)
    @Digits(integer = 3, fraction = 2)
    @CodeGenField("身高")
    private Double height;

    @CodeGenField("是否已婚")
    @AvailableValues(values={"0", "1"})
    private Boolean isMarried;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Boolean getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(Boolean isMarried) {
        this.isMarried = isMarried;
    }

    @Override
    public String toString() {
        return "SearchUserApiRequest{" +
                "country='" + country + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", isMarried=" + isMarried +
                '}';
    }
}
