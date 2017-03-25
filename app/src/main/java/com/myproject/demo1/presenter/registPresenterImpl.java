package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.myproject.demo1.model.User;
import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.view.RegistView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/2/16.
 */

public class registPresenterImpl implements RegistPresenter {
    private RegistView registView;

    public registPresenterImpl(RegistView registView) {
        this.registView = registView;
    }

    @Override
    public void regist(final String username, final String password) {
        //册成功显示对话框
        registView.showProgressDialog("正在注册");
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if (e == null) {
                    ThreadUtil.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance()
                                        .createAccount(username, password);
                                ThreadUtil.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.showProgressDialog("注册成功");
                                        registView.afterRegist(user, true, null);
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                user.delete();
                                ThreadUtil.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.showProgressDialog("注册失败");
                                        registView.afterRegist(user, false, e1.getMessage());
                                    }
                                });
                                //// TODO: 2017/2/16 删除注册 
                            }
                        }
                    });
                } else {
                    registView.hideProgressDialog();
                    registView.afterRegist(user, false, e.getMessage());
//注册失败
                }
            }
        });
    }
}
