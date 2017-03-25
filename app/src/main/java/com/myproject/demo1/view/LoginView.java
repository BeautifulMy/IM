package com.myproject.demo1.view;

import com.myproject.demo1.model.User;

/**
 * Created by Administrator on 2017/2/16.
 */

public interface LoginView extends BaseView{
    void afterLogin(User user ,boolean isSuccess,String msg);
}
