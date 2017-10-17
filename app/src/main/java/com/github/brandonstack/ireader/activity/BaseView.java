package com.github.brandonstack.ireader.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * 所有activity的基类，将重复代码放入
 * Created by 22693 on 2017/10/6.
 */

public abstract class BaseView extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    public void showProgress(boolean flag, String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(flag);
            mProgressDialog.setMessage(msg);
        }
        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog == null)
            return;
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract int getLayout();

    protected void checkPermission(Activity thisActivity,
                                   String permission,
                                   int requestCode,
                                   String errorText) {
        if (ContextCompat.checkSelfPermission(thisActivity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
            }
            //进行权限请求
            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{permission},
                    requestCode);
        } else {
            havePermission(requestCode);
        }
    }

    protected abstract void havePermission(int requestCode);
}
