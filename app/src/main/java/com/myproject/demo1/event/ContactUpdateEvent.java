package com.myproject.demo1.event;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ContactUpdateEvent {

        public boolean isAdded;
        public String username;

        public ContactUpdateEvent(boolean isAdded, String username) {
            this.isAdded = isAdded;
            this.username = username;
        }
}
