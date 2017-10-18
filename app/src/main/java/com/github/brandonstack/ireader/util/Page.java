package com.github.brandonstack.ireader.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/18.
 */

public class Page {
    @BindView(R.id.bookPage)
    EditText editText;

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
    public static synchronized Page getInstance() {
        return page;
    }

    public static synchronized Page createPage(Context context) {
        if (page == null) {
            page = new Page(context);
        }
        return page;
    }

    private Page(Context context) {
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

        editText.setLineSpacing(0,1.5f);
        mFontSize = editText.getTextSize();
        lineSpace = editText.getLineSpacingMultiplier();
    }

    public List<String> getPageFromBegin(Book book) {
        long begin = book.getBegin();
        if (begin <= 0) {
            mIsFirstPage = true;
            begin = 0;
        }
        List<String> list = new ArrayList<>();
        return list;
    }

    private long bookSize(Book book) {
        File file = new File(book.getPath());
        return file.length();
    }
}
