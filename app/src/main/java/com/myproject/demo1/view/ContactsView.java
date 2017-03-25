package com.myproject.demo1.view;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public interface ContactsView {
    void showContact(List<String> contacts);

    void updateContacts(boolean successUpdate);
    void afterContact(boolean isSuccess,String username);
}
