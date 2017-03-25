package com.myproject.demo1.utils;

import com.myproject.demo1.view.ContactsFragment;
import com.myproject.demo1.view.ConversationFragment;
import com.myproject.demo1.view.PluginBlankFragment;

/**
 * Created by Administrator on 2017/2/19.
 */

public class FragmentFactory {
    private static ConversationFragment conversationFragment;
    public  static ConversationFragment getConversationFragment(){
        if (conversationFragment==null){
            conversationFragment = new ConversationFragment();
        }
        return  conversationFragment;
    }
    private static ContactsFragment contactsFragment;
    public  static ContactsFragment getContactsFragment(){
        if (contactsFragment==null){
            contactsFragment = new ContactsFragment();
        }
        return  contactsFragment;
    }
    private static PluginBlankFragment pluginBlankFragment;
    public  static PluginBlankFragment getPluginBlankFragment(){
        if (pluginBlankFragment==null){
            pluginBlankFragment = new PluginBlankFragment();
        }
        return  pluginBlankFragment;
    }
}
