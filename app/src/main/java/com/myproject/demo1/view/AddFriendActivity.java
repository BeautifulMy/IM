package com.myproject.demo1.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myproject.demo1.R;
import com.myproject.demo1.adapter.SearchAdapter;
import com.myproject.demo1.model.User;
import com.myproject.demo1.presenter.AddFriendPresenterImp;
import com.myproject.demo1.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddFriendActivity extends AppCompatActivity implements AddFriendView, TextView.OnEditorActionListener {

    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.iv_search)
    ImageView ivSearch;
    @InjectView(R.id.iv_nodata)
    ImageView ivNodata;
    @InjectView(R.id.searchRecyclerView)
    RecyclerView searchRecyclerView;
    private AddFriendPresenterImp addFriendPresenterImp;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        tvTitle.setText("搜索");
        searchRecyclerView.setVisibility(View.GONE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        etUsername.setOnEditorActionListener(this);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length()==0){
            ivNodata.setVisibility(View.VISIBLE);
            searchRecyclerView.setVisibility(View.GONE);
        }else{
            search();
        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addFriendPresenterImp = new AddFriendPresenterImp(this);
    }
    @OnClick(R.id.iv_search)
    public void onClick(){
    search();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            search();
            return  true;

        }
        return false;
    }

    private void search() {
        String username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtils.showToast(this,"?????????");
        }
        addFriendPresenterImp.searchFriend(username);
   if (inputMethodManager.isActive()){
       inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY);
   }

    }

    @Override
    public void afterSearch(List<User> users, List<String> contacts, boolean isSuccess) {
        if (isSuccess){
            ivNodata.setVisibility(View.GONE);
            searchRecyclerView.setVisibility(View.VISIBLE);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            SearchAdapter searchAdapter = new SearchAdapter(users, contacts);
            searchRecyclerView.setAdapter(searchAdapter);
            searchAdapter.setOnAddFriendClickListener(new SearchAdapter.OnAddFriendClickListener() {
                @Override
                public void onClick(String username) {
                   addFriendPresenterImp.addContact(username);
                }
            });
        }else{
            ivNodata.setVisibility(View.VISIBLE);
            searchRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterAddContact(boolean success, String msg, String username) {
        if (success){
            Toast.makeText(this,"添加"+username+"请求发送成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"添加"+username+"请求发送失败"+msg,Toast.LENGTH_SHORT).show();
        }
    }
}
