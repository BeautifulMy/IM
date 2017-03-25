package com.myproject.demo1.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myproject.demo1.R;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.model.User;
import com.myproject.demo1.presenter.RegistPresenter;
import com.myproject.demo1.presenter.registPresenterImpl;
import com.myproject.demo1.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity implements RegistView, TextView.OnEditorActionListener {
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;
    private RegistPresenter registPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        etPwd.setOnEditorActionListener(this);
        registPresenter = new registPresenterImpl(this);
    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        regist();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_GO) {
            regist();
            return true;
        }
        return false;
    }

    private void regist() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (!StringUtils.checkUserName(username)) {
            showToast("用户名不合法");
            return;
        } else if (!StringUtils.checkPassWord(pwd)) {
            showToast("密码不合法");
            return;

        }
        registPresenter.regist(username, pwd);
    }

    @Override
    public void showProgressDialog(String msg) {
        showDialog(msg, false);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }

    @Override
    public void afterRegist(User user, boolean success, String errormsg) {
        if (success) {
            //跳转到登录界面
            saveUser(user);
            startActivity(LoginActivity.class, true);

        } else {
            showToast("注册失败:" + errormsg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registPresenter = null;
    }
}
