package com.myproject.demo1.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.myproject.demo1.R;
import com.myproject.demo1.adapter.TabSelectedListenerAdapter;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.event.ContactUpdateEvent;
import com.myproject.demo1.utils.FragmentFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Main2Activity extends BaseActivity implements EMContactListener, EMConnectionListener {

    @InjectView(R.id.fl_content)
    FrameLayout flContent;
    @InjectView(R.id.bottombar)
    BottomNavigationBar bottombar;
    @InjectView(R.id.activity_main2)
    LinearLayout activityMain2;
    private ArrayList<Fragment> fragments;
    private BadgeItem bafgritem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.inject(this);
         initListener();
        initFragment();
        initBottombar();
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
    updateUnreadMsgCount();
    }

    private void updateUnreadMsgCount() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (unreadMessageCount>99){
            bafgritem.setText("99+");
            bafgritem.show(true);
        }else if(unreadMessageCount>0){
            bafgritem.setText(unreadMessageCount+"").show(true);
        }else if(unreadMessageCount<0){
            bafgritem.hide(true);
        }
    }


    private void initListener() {
        EMClient.getInstance().contactManager().setContactListener(this);
    }

    private void initBottombar() {
        bafgritem = new BadgeItem();
        bafgritem.hide().setGravity(Gravity.RIGHT)
                .setBackgroundColor("#0DFFED").setTextColor(Color.BLACK).setHideOnSelect(false)
                .setAnimationDuration(100)
                .show();
        bottombar.setActiveColor("#00acff").setInActiveColor("#FF0033")
                .addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息").setBadgeItem(bafgritem)
                ).addItem(new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人")
        ).addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态"))
                .setFirstSelectedPosition(0).initialise();
        switchFragment(0);
        bottombar.setTabSelectedListener(new TabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(int position) {
                //showToast("被选中");

                switchFragment(position);
            }
        });

    }

    private void switchFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (i==position){
                if (fragment.isAdded()){
                    transaction.show(fragment);
                }else {
                    transaction.add(R.id.fl_content,fragment);
                }
            }else{
                if (fragment.isAdded()){
                    transaction.hide(fragment);
                }
            }

        }
        transaction.commitAllowingStateLoss();
    }

    private void initFragment() {
        ConversationFragment conversationFragment = FragmentFactory.getConversationFragment();
        ContactsFragment contactsFragment = FragmentFactory.getContactsFragment();
        PluginBlankFragment pluginBlankFragment = FragmentFactory.getPluginBlankFragment();
        fragments = new ArrayList<>();
        fragments.add(conversationFragment);
        fragments.add(contactsFragment);
        fragments.add(pluginBlankFragment);


    }

    @OnClick({R.id.fl_content, R.id.bottombar, R.id.activity_main2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_content:
                break;
            case R.id.bottombar:
                break;
            case R.id.activity_main2:
                break;
        }
    }

    @Override
    public void onContactAdded(String username) {
        EventBus.getDefault().post(new ContactUpdateEvent(true,username));
    }

    @Override
    public void onContactDeleted(String username) {
        EventBus.getDefault().post(new ContactUpdateEvent(false,username));
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //同意或者拒绝
        try {
            EMClient.getInstance().contactManager().acceptInvitation(username);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
//        ThreadUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                showToast("收到"+s+"发送过来的邀请："+s1);
//            }
//        });
        showToast("收到"+username+"发送过来的邀请："+reason);
    }

    @Override
    public void onFriendRequestAccepted(String username) {

    }

    @Override
    public void onFriendRequestDeclined(String username) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(int errorCode) {

    }
}
