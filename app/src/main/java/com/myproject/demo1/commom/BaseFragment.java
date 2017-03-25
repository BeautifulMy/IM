package com.myproject.demo1.commom;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myproject.demo1.R;

/**
 * Created by Administrator on 2017/2/19.
 */

public abstract class BaseFragment extends Fragment {

    public  TextView tvTitlte;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvTitlte = (TextView) getView().findViewById(R.id.tv_title);

        super.onViewCreated(view, savedInstanceState);
        initTitle();
    }

    public  abstract  void initTitle() ;
}
