package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.myproject.demo1.view.ConversationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/25.
 */

public class ConversationPresenterImp implements ConversationPresenter {
    private List<EMConversation> emConversationlist = new ArrayList<>();
    public ConversationView conversationView;
    public ConversationPresenterImp(ConversationView conversationView){
        this.conversationView = conversationView;
    }



    @Override
    public void initConversation() {
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
       emConversationlist.clear();
        emConversationlist.addAll(allConversations.values());
        Collections.sort(emConversationlist, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation lhs, EMConversation rhs) {
                return (int) (rhs.getLastMessage().getMsgTime()-lhs.getLastMessage().getMsgTime());
            }
        });
        conversationView.initData(emConversationlist);
    }

}
