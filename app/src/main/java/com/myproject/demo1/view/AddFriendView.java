package com.myproject.demo1.view;

import com.myproject.demo1.model.User;

import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public interface AddFriendView {
void afterSearch(List<User>users, List<String > contacts, boolean isSuccess);
    void afterAddContact(boolean success,String msg,String username);
}
