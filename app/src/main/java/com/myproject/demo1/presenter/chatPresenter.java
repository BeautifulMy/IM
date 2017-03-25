package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Administrator on 2017/3/24.
 */

public interface chatPresenter {
    void sendMessage(EMMessage emMessage);

    void initChatData(String username, boolean b);
}
