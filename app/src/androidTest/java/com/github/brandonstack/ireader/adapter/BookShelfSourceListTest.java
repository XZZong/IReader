package com.github.brandonstack.ireader.adapter;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.brandonstack.ireader.entity.Book;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 22693 on 2017/10/8.
 */
@RunWith(AndroidJUnit4.class)
public class BookShelfSourceListTest {
    @Test
    public void getInstance() {
        BookShelfSourceList list = BookShelfSourceList.getInstance();
//        for (Book book : list1) {
//        System.out.println(book.getName());
//            Log.d(this.getClass().getSimpleName(), book.getName());
//        }
        assertTrue(list.size() == 3);
    }

    @Test
    public void testAdd() {
        Book book = new Book();
        book.setName("1Q84");
        book.setCharset("UTF-8");
        BookShelfSourceList list = BookShelfSourceList.getInstance();
        list.add(book);
        isDatabaseTheSame(list);
    }

    @Test
    public void testDelete() {
        Book book = new Book();
        book.setId(4);
        BookShelfSourceList list = BookShelfSourceList.getInstance();
        list.delete(book);
        assertTrue(list.size() == 2);
        isDatabaseTheSame(list);
    }

    @Test
    public void testDeleteById() {
        BookShelfSourceList list = BookShelfSourceList.getInstance();
        list.delete(2);
        isDatabaseTheSame(list);
    }

    private void isDatabaseTheSame(BookShelfSourceList list) {
        List<Book> bookList = DataSupport.findAll(Book.class);
        assertTrue(bookList.size() == list.size());
        for (int i = 0; i < bookList.size(); i++) {
            assertTrue(bookList.get(i).getId() == list.getBook(i).getId());
        }
    }

}