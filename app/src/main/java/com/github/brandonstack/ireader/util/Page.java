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

import java.io.BufferedInputStream;
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

    public static String codeString(File file) {
        String code = null;
        try {
            BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
            int p = (bin.read() << 8) + bin.read();

            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public void getBookLength(Book book){
        File file = new File(book.getPath());
        try {
            String encode = codeString(file);
            values.put("type",encode);
            DataSupport.update(Book.class, values, book.getId());
            InputStreamReader input = new InputStreamReader(new FileInputStream(file), encode);
            while (true) {
                char[] buf = new char[30000];
                int result = input.read(buf);
                if (result == -1) {
                    input.close();
                    break;
                }
                String bufString = new String(buf);
                bufString = bufString.replaceAll("\r\n+\\s*", "\r\n\u3000\u3000");
                bufString = bufString.replaceAll("\u0000", "");
                buf = bufString.toCharArray();
                fileLength += buf.length;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageBegin(Book book) {
        long begin = 0;
        String path = book.getPath();
        number = 0;
        List<Long> pageBegin = new ArrayList<>();
        pageBegin.add(begin);
        while (begin < fileLength) {
            getPageFromBegin(begin,path);
            begin += number;
            pageBegin.add(begin);
        }
        mIsLastPage = false;
        book.setPageBegin(pageBegin);
    }

    public String getPageFromBegin(long begin, String path) {
        if (begin <= 0) {
            mIsFirstPage = true;
            begin = 0;
        }
        File file = new File(path);
//        fileLength = file.length() / 2;
        BufferedReader in = null;
        StringBuilder string = new StringBuilder("");
        try {
            int count = 0;
            number = 0;
            String encode = codeString(file);
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            if (begin > 1)
                in.skip(begin - 1);
            float width = 0;
            StringBuilder sb = new StringBuilder("");
            while (begin + number < fileLength && count < mLineCount) {
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
        if (begin + number >= fileLength)
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
        values.put("begin", preBegin);
        DataSupport.update(Book.class, values, book.getId());
        return getPageFromBegin(preBegin, book.getPath());
    }

    public String getNextPage(Book book) {
        List<Long> list = book.getPageBegin();
        long begin = book.getBegin();
        int index = list.indexOf(begin);
        long nextBegin = list.get(index + 1);
        book.setBegin(nextBegin);
        values.put("begin",nextBegin);
        DataSupport.update(Book.class,values,book.getId());
        return getPageFromBegin(nextBegin, book.getPath());
    }

    public String getPage(Book book) {
        values.put("begin",book.getBegin());
        DataSupport.update(Book.class, values, book.getId());
        return getPageFromBegin(book.getBegin(), book.getPath());
    }

    public boolean ismIsLastPage() {
        return this.mIsLastPage;
    }

    public long getFileLength() {
        return fileLength;
    }
}
