package com.myproject.demo1.event;

import com.hyphenate.chat.EMMessage;

/**
 * Created by taojin on 2016/9/12.11:56
 */
public class OnNewMsgEvent {
    public EMMessage emMessage;

    public OnNewMsgEvent(EMMessage emMessage){
        this.emMessage = emMessage;
    }
}
