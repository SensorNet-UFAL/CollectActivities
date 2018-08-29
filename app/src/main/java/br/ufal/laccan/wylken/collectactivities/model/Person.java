package br.ufal.laccan.wylken.collectactivities.model;

import android.content.Intent;

import java.io.Serializable;

public class Person implements Serializable {

    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private Integer tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
