package com.jtool.codegenbuilderplugin.test.api.response;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.NotNull;

public class User {

    @NotNull
    @CodeGenField("用户名称")
    private String name;

    @NotNull
    @CodeGenField("用户所在国家")
    private String country;

    @NotNull
    @CodeGenField("年龄")
    private Integer age;

    @NotNull
    @CodeGenField("身高")
    private Double height;

    @CodeGenField("是否已婚")
    private Boolean isMarried;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
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
        return "User{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", isMarried=" + isMarried +
                '}';
    }
}
