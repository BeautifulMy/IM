package com.myproject.demo1.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public interface chatView {
    void afterInitData(List<EMMessage>emMessageList,boolean isSmooth);
    void updateChatData(boolean success,String msg,EMMessage emMessage);
}
