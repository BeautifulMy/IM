package com.myproject.demo1.view;

import com.myproject.demo1.model.User;

/**
 * Created by Administrator on 2017/2/16.
 */

public interface RegistView extends BaseView{
       void afterRegist(User user,boolean success,String errormsg);
}
