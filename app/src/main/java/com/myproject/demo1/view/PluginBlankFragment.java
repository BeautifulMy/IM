package com.myproject.demo1.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.myproject.demo1.R;
import com.myproject.demo1.commom.BaseFragment;
import com.myproject.demo1.presenter.PluginPresenter;
import com.myproject.demo1.presenter.PluginPresenterImp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginBlankFragment extends BaseFragment implements PluginView{


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.btn_exit)
    Button btnExit;
    private PluginPresenter pluginPresenter;

    public PluginBlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plugin_blank, container, false);
        ButterKnife.inject(this, view);
        pluginPresenter = new PluginPresenterImp(this);
        String currentUser = EMClient.getInstance().getCurrentUser();
        btnExit.setText("退("+currentUser+")出");
        return view;
    }

    @Override
    public void initTitle() {
        tvTitlte.setText("联系人");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.btn_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                break;
            case R.id.iv_right:
                break;
            case R.id.btn_exit:
            pluginPresenter.exit();
                break;
        }
    }


    @Override
    public void afterLogout(boolean success, String msg) {
        Main2Activity activity= (Main2Activity) getActivity();
        activity.startActivity(LoginActivity.class,true);
        if (!success){
           activity.showToast(msg);
        }
    }
}
