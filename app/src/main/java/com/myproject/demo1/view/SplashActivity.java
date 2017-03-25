package com.myproject.demo1.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.myproject.demo1.R;
import com.myproject.demo1.commom.BaseActivity;
import com.myproject.demo1.presenter.SplashPresenterImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends BaseActivity implements SplashView {

    @InjectView(R.id.splash)
    ImageView splash;
    private SplashPresenterImpl splashPresenter;

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        splashPresenter = new SplashPresenterImpl(this);
        splashPresenter.isLogined();
    }

    @Override
    public void checkLogined(boolean isLogined) {
        if (isLogined) {
            startActivity(Main2Activity.class, true);
        } else {
            ObjectAnimator.ofFloat(splash, "alpha", 0, 1).setDuration(1000).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showToast("跳转到登录界面");
                    startActivity(LoginActivity.class,true);
                }
            }, 1000);
        }
    }
}
