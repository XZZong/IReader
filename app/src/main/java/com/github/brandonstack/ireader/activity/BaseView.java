package com.github.brandonstack.ireader.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 所有activity的基类，将重复代码放入
 * Created by 22693 on 2017/10/6.
 */

public abstract class BaseView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initData();
        initListener();
    }
    protected abstract void initData();
    protected abstract void initListener();
    protected abstract int getLayout();
}
