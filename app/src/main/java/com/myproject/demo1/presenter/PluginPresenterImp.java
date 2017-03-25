package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.myproject.demo1.adapter.EMCallBackAdapter;

import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.view.PluginView;

/**
 * Created by Administrator on 2017/2/19.
 */

public class PluginPresenterImp implements PluginPresenter {

    private  PluginView pluginView;

    public PluginPresenterImp(PluginView pluginView) {
        this.pluginView = pluginView;
    }

    @Override
    public void exit() {
        EMClient.getInstance().logout(true, new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                gotoLoginActivity(true,null);
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                gotoLoginActivity(false,s);
            }
        });
    }

    private void gotoLoginActivity(final boolean success, final String msg) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pluginView.afterLogout(success,msg);
            }
        });
    }
}
