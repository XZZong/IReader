package com.github.brandonstack.ireader.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.github.brandonstack.ireader.R;

import butterknife.BindView;

public class ScanActivity extends BaseView{
    @BindView(R.id.rv_folders)
    RecyclerView recyclerViewFolder;
    @BindView(R.id.btu_cancel)
    Button buttonCancle;
    @BindView(R.id.btu_ok)
    Button buttonOK;
    RecyclerView.LayoutManager layoutManagerFolder;
    RecyclerView.Adapter adapterFolder;

    @Override
    protected int getLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void havePermission(int requestCode) {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

}
