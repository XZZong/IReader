package com.github.brandonstack.ireader.adapter;

import com.github.brandonstack.ireader.entity.Book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 22693 on 2017/10/17.
 */

public class FolderSourceList {
    private Map<String, List<Book>> lib;
    private List<Book> books;
    private boolean[] checked;
    private static FolderSourceList list;
    private static BookShelfSourceList shelfList;

    private FolderSourceList() {
        lib = new LinkedHashMap<>();
        shelfList = BookShelfSourceList.getInstance();
        books = new ArrayList<>();
    }

    //一个工厂方法
    public static FolderSourceList getInstance() {
        if (list == null)
            list = new FolderSourceList();
        return list;
    }

    /**
     * 添加书本函数
     * 存在列表中忽略不计
     * 要不就根据key来进行添加
     */
    public void add(Book book) {
        //列表中是否存在
        if (!shelfList.contains(book)) {
            //如果列表不存在
            if (!lib.containsKey(book.getFolder()))
                lib.put(book.getFolder(), new ArrayList<Book>());
            lib.get(book.getFolder()).add(book);
        }
    }

    public void check(int position, boolean isChecked) {
        checked[position] = isChecked;
    }

    /**
     * 返回adapter函数的数据源
     * 使用场景仅仅一种，分类完成之后就不需要index，所以可以加入结束信号，然后将转为index，完成构建活动？
     * 后面继续讨论
     */
    public void convert() {
        for (Map.Entry<String, List<Book>> entry : lib.entrySet()) {
            books.add(setBook(entry.getKey()));
            books.addAll(entry.getValue());
        }
        lib.clear();
        checked = new boolean[books.size()];
    }

    public List<Book> getBooks() {
        return books;
    }

    private Book setBook(String folder) {
        Book book = new Book();
        book.setFolder(folder);
        return book;
    }

    public void addToBookShelf() {
        for (int i = 0; i < checked.length; i++) {
            if (checked[i])
                shelfList.add(books.get(i));
        }
        books.removeAll(books);
    }
}
