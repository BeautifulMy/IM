package com.myproject.demo1.adapter;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;

/**
 * Created by Administrator on 2017/2/19.
 */

public  abstract class TabSelectedListenerAdapter implements BottomNavigationBar.OnTabSelectedListener {
    @Override
    public  abstract void onTabSelected(int position) ;

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
