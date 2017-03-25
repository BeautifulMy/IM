package com.myproject.demo1.commom;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.myproject.demo1.MyApp;
import com.myproject.demo1.model.User;
import com.myproject.demo1.utils.ToastUtils;

public class BaseActivity extends AppCompatActivity {

    protected Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private static final String USERNAME_KEY = "username";
    private static final String PWD_KEY = "pwd";
    private MyApp application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        application = (MyApp) getApplication();
        application.addActivity(this);
    }

    public void saveUser(User user) {
        sharedPreferences.edit().putString(USERNAME_KEY, user.getUsername())
                .putString(PWD_KEY, user.getPassword()).commit();
    }

    public User getUser() {
        String username = sharedPreferences.getString(USERNAME_KEY, "");
        String pwd = sharedPreferences.getString(PWD_KEY, "");
        User user = new User(username, pwd);
        return user;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
        application.removeActivity(this);
    }

    public void showDialog(String msg, boolean isCancleable) {
        progressDialog.setCancelable(isCancleable);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public void startActivity(Class clazz, boolean isFinish) {
        startActivity(new Intent(this, clazz));
        if (isFinish) {
            finish();
        }
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }
}
