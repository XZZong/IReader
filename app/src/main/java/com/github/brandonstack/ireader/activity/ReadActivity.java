package com.github.brandonstack.ireader.activity;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.util.Page;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/17.
 */

public class ReadActivity extends BaseView {
    @BindView(R.id.bookPage)
    TextView textView;

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    private int downX = 0;
    private int downY = 0;
    private int moveX = 0;
    private int moveY = 0;
    private Boolean isMove = false;       //是否移动了
    private Boolean isNext = false;       //是否翻到下一页

    private Page page;
    private TouchListener mTouchListener;
    private Book book;

    @Override
    protected void initData() {
        //获取屏幕宽高
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        page = Page.getInstance(this, textView);
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        String show = page.getPageFromBegin(book.getBegin(),book.getPath());
        textView.setText(show);

        if (book.getPageBegin() == null) {
            page.setPageBegin(book);
        }
    }

    @Override
    protected void initListener() {
        mTouchListener = new TouchListener() {
            @Override
            public void center() {
                Toast.makeText(ReadActivity.this,"center setting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public Boolean prePage() {
                if (book.getBegin() <= 0) {
                    Toast.makeText(ReadActivity.this,"当前页已是第一页",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String show = page.getPrePage(book);
                textView.setText(show);
                return true;
            }

            @Override
            public Boolean nextPage() {
                if (page.ismIsLastPage()) {
                    Toast.makeText(ReadActivity.this,"当前页已是最后一页",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String show = page.getNextPage(book);
                textView.setText(show);
                return true;
            }

            @Override
            public void cancel() {

            }
        };
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_read;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
            downY = (int) event.getY();
            moveX = 0;
            moveY = 0;
            isMove = false;
            isNext = false;
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            isMove = true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isMove) {
                //是否点击了中间
                if(downX > mScreenWidth / 5 && downX < mScreenWidth * 4 / 5 && downY > mScreenHeight / 3 && downY < mScreenHeight * 2 / 3) {
                    if (mTouchListener != null) {
                        mTouchListener.center();
                    }
                    return true;
                }
                else if(x < mScreenWidth / 2) {
                    isNext = false;
                }
                else {
                    isNext = true;
                }

                if(isNext) {
                    Boolean isNext = mTouchListener.nextPage();
                    if (!isNext)
                        return true;
                }
                else {
                    Boolean isPre = mTouchListener.prePage();
                    if (!isPre)
                        return true;
                }
            }
        }
        return true;
    }

    public interface TouchListener{
        void center();
        Boolean prePage();
        Boolean nextPage();
        void cancel();
    }
}
