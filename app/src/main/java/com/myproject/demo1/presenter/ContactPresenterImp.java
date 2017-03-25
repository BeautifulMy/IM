package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.myproject.demo1.utils.DBUtils;
import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.view.ContactsView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class ContactPresenterImp implements ContactPresenter {
    private ArrayList<String> contactsList = new ArrayList<>();
    private ContactsView contactsView;

    public ContactPresenterImp(ContactsView contactsView) {
        this.contactsView = contactsView;


    }


    @Override
    public void initContact() {
        //初始化联系人先走本地缓存
        final List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
        contactsList.clear();
        contactsList.addAll(contacts);
        if (contactsList != null) {
            contactsView.showContact(contactsList);
        }
        //然后走网络
        然后走网络();

    }

    private void 然后走网络() {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
                    contactsList.clear();
                    contactsList.addAll(allContactsFromServer);
                    //保存到本地
                    DBUtils.saveContacts(EMClient.getInstance().getCurrentUser(),contactsList);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            contactsView.updateContacts(true);

                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsView.updateContacts(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateContact() {
        然后走网络();
    }

    @Override
    public void deleteContact(final String username) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    afterDelete(true, username);
                } catch (HyphenateException e) {
                    afterDelete(false, e.getMessage());

                }
            }


                private void afterDelete ( final boolean isSuccess, final String message){
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsView.afterContact(isSuccess, message
                            );
                        }
                    });
                }



        });
    }
}
