package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;

import com.myproject.demo1.adapter.EMCallBackAdapter;
import com.myproject.demo1.model.User;
import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.view.LoginView;

/**
 * Created by Administrator on 2017/2/16.
 */

public class loginPresenterImpl implements LoginPresenter {
    private LoginView loginView;

    @Override
    public void login(final String userName, final String password) {
        loginView.showProgressDialog("正在登录");
        EMClient.getInstance().login(userName, password, new EMCallBackAdapter(){

            @Override
            public void onSuccess() {
                super.onSuccess();
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginView.hideProgressDialog();
                        loginView.afterLogin(new User(userName,password),true,null);
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                super.onError(i, s);
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginView.hideProgressDialog();
                        loginView.afterLogin(new User(userName,password),false,s);
                    }
                });
            }
        });
    }

    public loginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }
}
