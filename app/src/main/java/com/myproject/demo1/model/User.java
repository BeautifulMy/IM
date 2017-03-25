package com.myproject.demo1.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/2/16.
 */

public class User extends BmobUser {


    private String password2;

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.password2 = password;
    }

    public User() {
    }

    public User(String username, String pwd) {
        setUsername(username);
        setPassword(pwd);
    }

    public String getPassword() {
        return password2;
    }
}
