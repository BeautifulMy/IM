package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.myproject.demo1.view.SplashView;

/**
 * Created by Administrator on 2017/2/16.
 */

public class SplashPresenterImpl implements SplashPreshter {
    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        this.mSplashView = splashView;
    }

    @Override
    public void isLogined() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            mSplashView.checkLogined(true);
        } else {
            //没有登录
            mSplashView.checkLogined(false);
        }
    }
}
