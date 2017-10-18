package com.github.brandonstack.ireader.activity;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.adapter.FolderItemAdapter;
import com.github.brandonstack.ireader.util.Content;

import butterknife.BindView;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ScanActivity extends BaseView {
    private static final int READERFILES = 1;
    public static final int READ_EXTERNAL_REQUEST_CODE = 10;


    @BindView(R.id.rv_folders)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //    @BindView(R.id.btu_cancel)
//    Button buttonCancle;
//    @BindView(R.id.btu_ok)
//    Button buttonOK;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter adapterFolder;

    @Override
    protected void initData() {
        setSupportActionBar(mToolbar);
        //添加后退按钮
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //检查是否有权限，如果有，那么就搜集数据
        checkPermission(ScanActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                READ_EXTERNAL_REQUEST_CODE,
                "需要扫描储存卡的权限");

        //adapter
        adapterFolder = new FolderItemAdapter();
        mRecyclerView.setAdapter(adapterFolder);

        //layoutmanager
        mLayoutManager = new LinearLayoutManager(ScanActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        buttonCancle.setVisibility(View.INVISIBLE);
//        buttonOK.setVisibility(View.INVISIBLE);
    }

    private void scanFiles() {
        showProgress(true, "Loading");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Content content = new Content(ScanActivity.this);
                content.queryFiles();
                handler.sendEmptyMessage(READERFILES);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hideProgress();
            adapterFolder.notifyDataSetChanged();
//            buttonCancle.setVisibility(View.VISIBLE);
//            buttonOK.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void havePermission(int requestCode) {
        switch (requestCode) {
            case READ_EXTERNAL_REQUEST_CODE: {
                scanFiles();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_REQUEST_CODE: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    havePermission(READ_EXTERNAL_REQUEST_CODE);
                } else {
                    makeText(this, "Permission Rejected", LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }


    /**
     * 解析菜单选项
     *
     * @param menu 菜单
     * @return 好像有个调用顺序什么的
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ok:
                Toast.makeText(this, "" + R.id.home, Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
                onBackPressed();
                break;
            default:
                Toast.makeText(this, R.id.home + " " + item.getItemId(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
