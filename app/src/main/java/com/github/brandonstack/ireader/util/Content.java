package com.github.brandonstack.ireader.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;

import com.github.brandonstack.ireader.adapter.FolderItemAdapter;
import com.github.brandonstack.ireader.entity.Book;

/**
 * Created by 22693 on 2017/10/8.
 */

public class Content {
    private final static String[] FILETYPES = new String[]{"txt"};

    private Context context;
    //    List<Book> books;
    private FolderItemAdapter adapter;


    public Content(Context context, FolderItemAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
//        books = new ArrayList<>();
    }

    public void queryFiles() {

        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA
        };

        //get Cursor
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                // TODO: 2017/10/17 重构这里这个文档
                MediaStore.Files.FileColumns.DATA + " like '%txt'",
                null,
                null

        );

        //get contents
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int dataIndex = cursor
                        .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                do {
                    String path = cursor.getString(dataIndex);
                    int slash = path.lastIndexOf("/");
                    String name = path.substring(slash + 1);
                    String folder = path.substring(path.indexOf('0') + 2, slash + 1);
//                    int dot = name.indexOf(".");
                    int dot = name.lastIndexOf(".");
                    Book book = new Book();
                    book.setName(name);
                    book.setFolder(folder);
                    book.setPath(path);
                    book.setType(name.substring(dot + 1));
                    adapter.add(book);
//                    books.add(book);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        adapter.convert();
    }
}
