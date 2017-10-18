package com.github.brandonstack.ireader;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.github.brandonstack.ireader.activity.BaseView;
import com.github.brandonstack.ireader.util.Page;
import com.github.brandonstack.ireader.view.PageWidget;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/17.
 */

public class ReadActivity extends BaseView {
    @BindView(R.id.bookPage)
    PageWidget bookPage;

    private Page page;

    @Override
    protected void initData() {
        page = Page.getInstance();
    }

    @Override
    protected void initListener() {
        bookPage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {

            }

            @Override
            public Boolean prePage() {
                return true;
            }

            @Override
            public Boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_read;
    }
}
