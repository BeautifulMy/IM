package com.myproject.demo1.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.myproject.demo1.R;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.model.User;
import com.myproject.demo1.presenter.LoginPresenter;
import com.myproject.demo1.presenter.loginPresenterImpl;
import com.myproject.demo1.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {
    private static final int REQUEST_PERMISSION_SDCARD = 1;
    @InjectView(R.id.userName)
    EditText userName;
    @InjectView(R.id.passWord)
    EditText passWord;
    @InjectView(R.id.tv_newuser)
    TextView tvNewuser;
    @InjectView(R.id.activity_login)
    LinearLayout activityLogin;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        loginPresenter = new loginPresenterImpl(this);
        passWord.setOnEditorActionListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = getUser();
        userName.setText(user.getUsername());
        passWord.setText(user.getPassword());
    }

    @OnClick({R.id.tv_newuser, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_newuser:
                showToast("跳转到新用户注册");
                startActivity(RegistActivity.class,false);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_GO) {
            login();
            return true;
        }
        return true;

    }

    private void login() {
        String username = userName.getText().toString().trim();
        String password = passWord.getText().toString().trim();
        if (!StringUtils.checkUserName(username)) {
            showToast("用户名不合法");
            return;
        } else if (!StringUtils.checkPassWord(password)) {
            showToast("密码不合法");
            return;
        }
        //检查权限
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED) {
            //没有被授权
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_SDCARD);
            return;
        }else{
            loginPresenter.login(username, password);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION_SDCARD){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&grantResults.equals(PermissionChecker.PERMISSION_GRANTED)){
                login();
            }
        }
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
    public void afterLogin(User user, boolean isSuccess, String msg) {
        if (isSuccess) {
            EMClient.getInstance().chatManager().loadAllConversations();
            saveUser(user);
            //跳转到主界面
            startActivity(Main2Activity.class,true);
        } else {
            showToast(msg);

        }
    }
}
