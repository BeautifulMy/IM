package com.myproject.demo1.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.myproject.demo1.R;
import com.myproject.demo1.adapter.ConversationAdapter;
import com.myproject.demo1.commom.BaseFragment;
import com.myproject.demo1.presenter.ConversationPresenter;
import com.myproject.demo1.presenter.ConversationPresenterImp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationView {


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.conversationRecyclerView)
    RecyclerView conversationRecyclerView;
    private ConversationPresenter conversationPresenterImp;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.inject(this, view);
        conversationPresenterImp = new ConversationPresenterImp(this);
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        conversationPresenterImp.initConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        conversationPresenterImp.initConversation();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        conversationPresenterImp.initConversation();
    }

    @Override
    public void initTitle() {
        tvTitlte.setText("消息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData(List<EMConversation> emConversationlist) {
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ConversationAdapter conversationAdapter = new ConversationAdapter(emConversationlist);
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationAdapter.setOnConversationItemClickListener(new ConversationAdapter.OnConversationItemClickListener() {
            @Override
            public void onItemClick(String username) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
}
