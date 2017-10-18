package com.github.brandonstack.ireader.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */

public class Page {
    TextView textView;

    private static Page page;
    private boolean mIsFirstPage;  //当前页是否为第一页
    private boolean mIsLastPage;   //当前页是否为最后一页
    private int mWidth;     //页面宽度
    private int mHeight;    //页面高度
    private float marginWidth;   // 左右与边缘的距离
    private float marginHeight;  // 上下与边缘的距离
    private float mVisibleWidth;   // 绘制内容的宽
    private float mVisibleHeight;   // 绘制内容的高
    private float lineSpace;       //行间距
    private float mFontSize;       //字体大小
    private int mLineCount;        //每一页的行数
    public static synchronized Page getInstance(Context context, TextView textView) {
        if (page == null) {
            page = new Page(context, textView);
        }
        return page;
    }

    private Page(Context context, TextView textView1) {
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;

        marginWidth = context.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = context.getResources().getDimension(R.dimen.readingMarginHeight);

        mVisibleWidth = mWidth - 2 * marginWidth;
        mVisibleHeight = mHeight - 2 * marginHeight;

        textView = textView1;
        textView.setLineSpacing(0,1.5f);
        mFontSize = textView.getTextSize();
        lineSpace = textView.getLineSpacingMultiplier();
        mLineCount =(int)( mVisibleHeight / (mFontSize * lineSpace));
    }

    public String getPageFromBegin(Book book) {
        long begin = book.getBegin();
        if (begin <= 0) {
            mIsFirstPage = true;
            begin = 0;
        }
        File file = new File(book.getPath());
        int count = 0;
        long number = 0;
        BufferedReader in = null;
        StringBuilder string = new StringBuilder("");
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            float width = mFontSize;
            StringBuilder sb = new StringBuilder("");
            while (begin < file.length() && count < mLineCount) {
                number++;
                char c = (char)in.read();
                if(c == '\n' || width > mVisibleWidth) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                        count++;
                        string.append(sb);
                        if (c == '\n') {
                            width = mFontSize * 3;
                            sb = new StringBuilder("        ");
                        }
                        else {
                            width = mFontSize * 2;
                            sb = new StringBuilder(c + "");
                        }
                    }
                }
                else {
                    sb.append(c);
                    width += mFontSize;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        book.setBegin(begin + number + 1);
        return string.toString();
    }
}
