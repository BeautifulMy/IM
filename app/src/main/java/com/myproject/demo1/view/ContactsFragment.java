package com.myproject.demo1.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.demo1.R;
import com.myproject.demo1.adapter.ContactAdapter;
import com.myproject.demo1.commom.BaseFragment;
import com.myproject.demo1.event.ContactUpdateEvent;
import com.myproject.demo1.presenter.ContactPresenterImp;
import com.myproject.demo1.utils.ToastUtils;
import com.myproject.demo1.widget.ContactListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends BaseFragment implements ContactsView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.contactListView)
    com.myproject.demo1.widget.ContactListView   contactListView;
    private ContactPresenterImp contactPresenterImp;
    private ContactAdapter contactAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contactPresenterImp = new ContactPresenterImp(this);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.inject(this, view);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactPresenterImp.initContact();
        contactListView.setOnRefreshListener(this);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactUpdateEvent contactUpdateEvent){
       contactPresenterImp.initContact();
    }

    @Override
    public void initTitle() {
        tvTitlte.setText("联系人");
    }

    @Override
    public void showContact(List<String> contacts) {
        contactAdapter = new ContactAdapter(contacts);
        contactListView.setAdapter(contactAdapter);
        contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String username) {
                Main2Activity main2Activity = (Main2Activity) getActivity();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username",username);

                main2Activity.startActivity(intent);
            }

            @Override
            public void onItemLongClick(final String username) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("您确定和"+username+"友尽了吗？")
                        .setNegativeButton("友尽", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contactPresenterImp.deleteContact(username);
                            }
                        })
                        .setPositiveButton("再续前缘", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();

                alertDialog.show();
            }
        });
    }

    @Override
    public void updateContacts(boolean success) {
        if (success){
            contactListView.setRefreshing(false);
            contactAdapter.notifyDataSetChanged();
        }else{
            ToastUtils.showToast(getActivity(),"同步失败");
        }

    }

    @Override
    public void afterContact(boolean isSuccess, String username) {
        if (isSuccess){
           ToastUtils.showToast(getActivity(),"删除好友成功，还可以再添加为好友");
        }else {
            ToastUtils.showToast(getActivity(),"删除失败，请稍后再删除："+username);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        contactPresenterImp.updateContact();
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.iv_right:
              startActivity(new Intent(getActivity(),AddFriendActivity.class));
              break;
      }
    }


}
