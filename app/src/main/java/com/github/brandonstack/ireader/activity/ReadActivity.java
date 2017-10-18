package com.github.brandonstack.ireader.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.activity.BaseView;
import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.util.Page;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/17.
 */

public class ReadActivity extends BaseView {
    @BindView(R.id.bookPage)
    EditText editText;

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    private int downX = 0;
    private int downY = 0;
    private int moveX = 0;
    private int moveY = 0;
    private Boolean isMove = false;       //是否移动了
    private Boolean isNext = false;       //是否翻到下一页
    private Boolean noNext = false;       //是否没下一页或者上一页

    private Page page;
    private TouchListener mTouchListener;
    private Book book;

    @Override
    protected void initData() {
        page = Page.getInstance();
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");
        List<String> list = page.getPageFromBegin(book);
        editText.setText(list.get(0));
    }

    @Override
    protected void initListener() {
        setTouchListener(new TouchListener() {
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
            noNext = false;
            isNext = false;
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

    public void setTouchListener(TouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    public interface TouchListener{
        void center();
        Boolean prePage();
        Boolean nextPage();
        void cancel();
    }
}
