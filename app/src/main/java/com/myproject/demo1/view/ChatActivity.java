package com.myproject.demo1.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.myproject.demo1.R;
import com.myproject.demo1.adapter.ChatAdapter;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.presenter.chatPresenter;
import com.myproject.demo1.presenter.chatPresenterImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements chatView, TextView.OnEditorActionListener {
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;
    @InjectView(R.id.et_msg)
    EditText etMsg;
    @InjectView(R.id.btn_send)
    Button btnSend;
    private chatPresenter chatPresenterImp;
    private String username;
    ChatAdapter chatAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        chatPresenterImp = new chatPresenterImp(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username == null) {
            finish();
            showToast("没有什么可聊的");
            return;
        }
        tvTitle.setText("与" + username + "聊天中");
        etMsg.setOnEditorActionListener(this);
        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });
        EventBus.getDefault().register(this);
        //初始化聊天记录\
       chatPresenterImp.initChatData(username, false);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //更新消息
        if (username.equals(emMessage.getUserName())){
            //更新recycleview
            chatPresenterImp.initChatData(username,true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_left, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        String msg = etMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)){
            return;
        }
        etMsg.getText().clear();
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg,username);
        chatPresenterImp.sendMessage(emMessage);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND) {
            sendMsg();
            return true;
        }
        return false;
    }

    @Override
    public void afterInitData(List<EMMessage> emMessageList,boolean isSmooth) {
        if (chatAdapter==null){
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            chatAdapter = new ChatAdapter(emMessageList);
            chatRecyclerView.setAdapter(chatAdapter);
        }else {
            chatAdapter.notifyDataSetChanged();
        }
        if (isSmooth){
            chatRecyclerView.smoothScrollToPosition(emMessageList.size()-1);
        }else {
            chatRecyclerView.scrollToPosition(emMessageList.size()-1);
        }


    }

    @Override
    public void updateChatData(boolean success, String msg, EMMessage emMessage) {
        if (!success){
            showToast(msg);
        }
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE);

    }
}
