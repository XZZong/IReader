package com.github.brandonstack.ireader;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.brandonstack.ireader.entity.Book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.litepal.crud.DataSupport;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 22693 on 2017/10/7.
 */

@RunWith(AndroidJUnit4.class)
public class TestFindAll {
    List<Book> list1, list2;

    @Test
    public void testFindAllObjectsEquals() {
        list1 = DataSupport.findAll(Book.class);
        list2 = DataSupport.findAll(Book.class);
        for (int i = 0; i < list1.size(); i++) {
            Log.e("names", list1.get(i).getName() + " " + list2.get(i).getName());
            Log.e("test2", list1.get(i).toString());
            Log.e("test2", list2.get(i).toString());
        }
    }
}
