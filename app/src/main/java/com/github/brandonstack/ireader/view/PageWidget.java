package com.github.brandonstack.ireader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by admin on 2017/10/18.
 */

public class PageWidget extends View {
    private Context mContext;

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    private int downX = 0;
    private int downY = 0;
    private int moveX = 0;
    private int moveY = 0;
    private Boolean isMove = false;       //是否移动了
    private Boolean isNext = false;       //是否翻到下一页
    private Boolean noNext = false;       //是否没下一页或者上一页

    Bitmap mCurPageBitmap = null; // 当前页
    Bitmap mNextPageBitmap = null;

    private TouchListener mTouchListener;

    public PageWidget(Context context) {
        this(context, null);
    }

    public PageWidget(Context context, AttributeSet set) {
        this(context, set, 0);
    }

    public PageWidget(Context context, AttributeSet set, int defStyleAttr) {
        super(context,set,defStyleAttr);
        mContext = context;
        initPage();
    }

    private void initPage() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);      //android:LargeHeap=true  use in  manifest application
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
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
