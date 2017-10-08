package com.github.brandonstack.ireader.adapter;

import android.support.v7.widget.RecyclerView;

import com.github.brandonstack.ireader.entity.Book;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 22693 on 2017/10/8.
 */

public class BookShelfSourceList {
    private boolean changed;
    private List<Book> books;
    private static BookShelfSourceList list;
    RecyclerView.Adapter adapter;

    private BookShelfSourceList() {
        changed = false;
        books = DataSupport.findAll(Book.class);
    }

    public List<Book> getBooks() {
        return books;
    }

    public static BookShelfSourceList getInstance() {
        if (list == null)
            list = new BookShelfSourceList();
        return list;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public Book getBook(int i) {
        return books.get(i);
    }

    public int size() {
        return books.size();
    }

    public void add(Book book) {
        book.save();
        books.add(book);
        changed = true;
    }

    /**
     * 删除真实存在的某个对象
     *
     * @param book 从list中得到的某个对象
     */
    public void delete(Book book) {
        if (books.contains(book)) {
            books.remove(book);
            DataSupport.delete(Book.class, book.getId());
            changed = true;
        } else delete(book.getId());
    }

    public void delete(long id) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                DataSupport.delete(Book.class, id);
                books.remove(i);
                changed = true;
                break;
            }
        }
        throw new RuntimeException("No record with id " + id);
    }
    public void refresh(){
        if (changed){
            adapter.notifyDataSetChanged();
            changed = false;
        }
    }
}