package com.myproject.demo1.presenter;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.myproject.demo1.adapter.EMCallBackAdapter;
import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.view.chatView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class chatPresenterImp implements chatPresenter {
    private chatView chatView;
    public chatPresenterImp(chatView chatView){
        this.chatView= chatView;
    }
    @Override
    public void sendMessage(final EMMessage emMessage) {
        //先添加到emMessageList中
        emMessage.setStatus(EMMessage.Status.INPROGRESS);//正在发送
        emMessageList.add(emMessage);
        // 然后让Adapter Notify一下
        chatView.updateChatData(true,null,emMessage);
        //发送的时候监听消息的发送状态 正在发送 成功  失败
        emMessage.setMessageStatusCallback(new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                afterSend(true,null,emMessage);
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                afterSend(false,s,emMessage);
            }
        });
        //通过环信sdk发送出去
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }
    private List<EMMessage> emMessageList = new ArrayList<>();

    @Override
    public void initChatData(String username, boolean isSmooth) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation==null){
            emMessageList.clear();
           chatView.afterInitData(emMessageList,false);

        }else {

            //既然数据已经显示了，就认为用户已经读取了数据
            conversation.markAllMessagesAsRead();

            int allMsgCount = conversation.getAllMsgCount();//统计数据库中我和username的所有的聊天的消息个数

            //需要获取最新的一条消息
            EMMessage lastMessage = conversation.getLastMessage();

            allMsgCount = allMsgCount>19?19:allMsgCount;

            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), allMsgCount);

            emMessageList.clear();

            emMessageList.addAll(emMessages);
            emMessageList.add(lastMessage);

            chatView.afterInitData(emMessageList,isSmooth);

        }

    }
    private void afterSend(final boolean success, final String msg, final EMMessage emMessage) {
        //重新 让Adapter Notify
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatView.updateChatData(success, msg, emMessage);
            }
        });
    }

}
