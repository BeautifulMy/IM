package com.myproject.demo1.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.myproject.demo1.model.User;
import com.myproject.demo1.utils.DBUtils;
import com.myproject.demo1.utils.ThreadUtil;
import com.myproject.demo1.utils.ToastUtils;
import com.myproject.demo1.view.AddFriendView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/3/18.
 */

public class AddFriendPresenterImp implements AddFrientPresenter {
    private final AddFriendView addFriendView;

    public AddFriendPresenterImp(AddFriendView addFriendView){
        this.addFriendView = addFriendView;
    }

    @Override
    public void searchFriend(String userName) {
        //去Bmob中搜索
        BmobQuery<User>query = new BmobQuery<>();
        //以username开头并且不能包含自己
        query.addWhereContains("username",userName);
        query.addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null&&list.size()>0){
                    //成功
                    List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
                    addFriendView.afterSearch(list,contacts,true);
                }else{

                }
            }
        });
    }

    @Override
    public void addContact(final String username) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                //添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(username,"想和你一起玩，赶紧添加我为好友吧。");
                    afterAdd(true,null,username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    afterAdd(false,e.getMessage(),username);
                }
            }
        });
    }
    private void afterAdd(final boolean success, final String msg, final String username){
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addFriendView.afterAddContact(success, msg, username);
            }
        });
    }
}
