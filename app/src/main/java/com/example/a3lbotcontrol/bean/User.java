package com.example.a3lbotcontrol.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者：Created by Administrator on 2020/9/9.
 * 邮箱：
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private int height;
    private int weight;
    private String sex;
    @NotNull
    private String phoneNumber;

    @Generated(hash = 59235562)
    public User(Long id, int height, int weight, String sex,
                @NotNull String phoneNumber) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
    }

    public User(int height, int weight, String sex,
                @NotNull String phoneNumber) {
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
    }


    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
