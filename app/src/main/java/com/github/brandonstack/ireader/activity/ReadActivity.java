package com.github.brandonstack.ireader.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.util.Page;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class ReadActivity extends BaseView {
    @BindView(R.id.bookPage)
    TextView textView;
    @BindView(R.id.process1)
    TextView process;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.rl_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.bookpop_bottom)
    LinearLayout bookpop_bottom;
    @BindView(R.id.tv_pre)
    TextView tv_pre;
    @BindView(R.id.sb_progress)
    SeekBar sb_progress;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.tv_directory)
    TextView tv_directory;
    @BindView(R.id.tv_dayornight)
    TextView tv_dayornight;
    @BindView(R.id.tv_pageMode)
    TextView tv_pagemode;
    @BindView(R.id.tv_setting)
    TextView tv_setting;

    private int mScreenWidth = 0; // 屏幕宽
    private int mScreenHeight = 0; // 屏幕高
    private int downX = 0;
    private int downY = 0;
    private int moveX = 0;
    private int moveY = 0;
    private Boolean isMove = false;       //是否移动了
    private Boolean isNext = false;       //是否翻到下一页
    private boolean isShow = false;

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
        page.getBookLength(book);
        String show = page.getPageFromBegin(book.getBegin(),book.getPath());
        textView.setText(show);

        if (book.getPageBegin() == null) {
            page.setPageBegin(book);
        }
//        File file = new File(book.getPath());
//        Log.i("length",file.length() + "");
        setProcess();
    }

    @Override
    protected void initListener() {
        mTouchListener = new TouchListener() {
            @Override
            public void center() {
                if (isShow)
                    hideReadSetting();
                else
                    showReadSetting();
            }

            @Override
            public Boolean prePage() {
                //如果设置界面已打开，点击无效
                if (isShow)
                    return true;
                if (book.getBegin() <= 0) {
                    Toast.makeText(ReadActivity.this,"当前页已是第一页",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String show = page.getPrePage(book);
                textView.setText(show);
                setProcess();
                return true;
            }

            @Override
            public Boolean nextPage() {
                if (isShow)
                    return true;
                if (page.ismIsLastPage()) {
                    Toast.makeText(ReadActivity.this,"当前页已是最后一页",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String show = page.getNextPage(book);
                textView.setText(show);
                setProcess();
                return true;
            }

            @Override
            public void cancel() {

            }
        };
        sb_progress.setMax(10000);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rl_progress.setVisibility(View.VISIBLE);
                String progress = String.valueOf(i / 100.0);
                progress += "%";
                tv_progress.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rl_progress.setVisibility(View.GONE);
                List<Long> list = book.getPageBegin();
                int index = list.size() * seekBar.getProgress() / 10000 - 1;
                index = Math.max(0,index);
                book.setBegin(list.get(index));
                String show = page.getPage(book);
                textView.setText(show);
                setProcess();
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
                else isNext = x >= mScreenWidth / 2;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                hideReadSetting();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("DefaultLocale")
    private void setProcess () {
        float pro = (float) book.getBegin() / page.getFileLength() * 100;
        String pr = String.format("%.2f%%   ", pro);
        sb_progress.setProgress((int) pro * 100);
        process.setText(pr);
    }

    private void showReadSetting() {
        isShow = true;
        rl_bottom.setVisibility(View.VISIBLE);
    }

    private void hideReadSetting() {
        isShow = false;
        rl_bottom.setVisibility(View.GONE);
    }

    private interface TouchListener{
        void center();
        Boolean prePage();
        Boolean nextPage();
        void cancel();
    }
}
