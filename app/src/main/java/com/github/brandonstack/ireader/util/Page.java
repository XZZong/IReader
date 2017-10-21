package com.github.brandonstack.ireader.util;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/18.
 */

public class Page {
    TextView textView;

    private Paint mPaint;
    private static Page page;
    private boolean mIsFirstPage;  //当前页是否为第一页
    private boolean mIsLastPage = false;   //当前页是否为最后一页
    private long number = 0;     //当前页字数
    private int mWidth;     //页面宽度
    private int mHeight;    //页面高度
    private float marginWidth;   // 左右与边缘的距离
    private float marginHeight;  // 上下与边缘的距离
    private float mVisibleWidth;   // 绘制内容的宽
    private float mVisibleHeight;   // 绘制内容的高
    private float lineSpace;       //行间距
    private float mFontSize;       //字体大小
    private int mLineCount;        //每一页的行数
    private long fileLength;       //
    ContentValues values = new ContentValues();
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
        mLineCount =(int)(mVisibleHeight / (mFontSize * lineSpace) - 3);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
        mPaint.setTypeface(textView.getTypeface());
    }

    public void setPageBegin(Book book) {
        long begin = 0;
        String path = book.getPath();
        number = 0;
        List<Long> pageBegin = new ArrayList<>();
        pageBegin.add(begin);
        while (begin + number < fileLength) {
            getPageFromBegin(begin,path);
            begin += number;
            pageBegin.add(begin);
        }
        mIsLastPage = false;
        book.setPageBegin(pageBegin);
//        values.put("pageBegin",pageBegin);
//        DataSupport.update(Book.class,values,book.getId());
    }

    public String getPageFromBegin(long begin, String path) {
//        long begin = book.getBegin();
        if (begin <= 0) {
            mIsFirstPage = true;
            begin = 0;
        }
//        File file = new File(book.getPath());
        File file = new File(path);
        fileLength = file.length();
        BufferedReader in = null;
        StringBuilder string = new StringBuilder("");
        try {
            int count = 0;
            number = 0;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            if (begin > 1)
                in.skip(begin - 1);
            float width = 0;
            StringBuilder sb = new StringBuilder("");
            while (begin + number < file.length() && count < mLineCount) {
                number++;
                char c = (char)in.read();
                float widthChar = mPaint.measureText(c + "");
                width += widthChar;
                if(c == '\n' || c == '\r' || width > mVisibleWidth) {
                    if (sb.length() > 0) {
                        sb.append('\n');
                        count++;
                        string.append(sb);
                        if (c == '\r') {
                            width = 0;
                            sb = new StringBuilder("");
                        }
                        else {
                            width = widthChar;
                            sb = new StringBuilder(c + "");
                        }
                    }
                }
                else {
                    sb.append(c);
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
        if (begin + number >= file.length())
            mIsLastPage = true;
        return string.toString();
    }

    public String getPrePage(Book book) {
        List<Long> list = book.getPageBegin();
        long begin = book.getBegin();
        int index = list.indexOf(begin);
        long preBegin = list.get(index - 1);
        book.setBegin(preBegin);
        if (mIsLastPage)
            mIsLastPage = false;
//        values.put("begin",book.getBegin());
//        DataSupport.update(Book.class,values,book.getId());
        return getPageFromBegin(preBegin, book.getPath());
    }
/*
    public String getPrePage(Book book) {
        long begin = book.getBegin() * 2;
        StringBuilder string = new StringBuilder("");
        try {
            int count = 0;
            while (count < mLineCount && begin > 0) {
                byte[] buffer = readParagraphBack((int)begin, book);
                String temp = new String(buffer, "gbk");
                temp = temp.replace("\r\n","");
                if (temp.length() == 0)
                    continue;
                begin -= buffer.length;
                float width = 2 * mFontSize;
                int countTemp = 0;
                StringBuilder sb = new StringBuilder("");
                for (char c:temp.toCharArray()) {
                    float widthChar = mPaint.measureText(c + "");
                    width += widthChar;
                    if(width < mVisibleWidth) {
                        sb.append(c);
                    }
                    else {
                        countTemp++;
                        sb.append('\n');
                        sb.append(c);
                        width = widthChar;
                    }
                }
                string.insert(0,sb);
                count += countTemp;
            }
            while (count > mLineCount) {
                int index = string.indexOf("\n");
                string.delete(0,index + 1);
                count--;
            }
            return string.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] readParagraphBack(int begin, Book book){
        RandomAccessFile randomAccess = null;
        try {
            randomAccess = new RandomAccessFile(book.getPath(), "r");
            MappedByteBuffer mapped = randomAccess.getChannel().map(FileChannel.MapMode.READ_ONLY,0, fileLength);
            byte b0 ;
            int i = begin - 1;
            while(i > 0){
                b0 = mapped.get(i);
                if(b0 == 0x0a && i != begin - 1){
                    i++;
                    break;
                }
                i--;
            }
            int nParaSize = begin - i;
            byte[] buf = new byte[nParaSize];
            for (int j = 0; j < nParaSize; j++) {
                buf[j] = mapped.get(i + j);
            }
//            mapped.clear();
//            randomAccess.close();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    public String getNextPage(Book book) {
        List<Long> list = book.getPageBegin();
        long begin = book.getBegin();
        int index = list.indexOf(begin);
        long nextBegin = list.get(index + 1);
        book.setBegin(nextBegin);
//        values.put("begin",nextBegin);
//        DataSupport.update(Book.class,values,book.getId());
        return getPageFromBegin(nextBegin, book.getPath());
    }

    public boolean ismIsLastPage() {
        return mIsLastPage;
    }
}
