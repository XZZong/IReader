package com.github.brandonstack.ireader.adapter;

import android.support.v7.widget.RecyclerView;

import com.github.brandonstack.ireader.entity.Book;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22693 on 2017/10/19.
 */

public class IndexSourceList
        implements ItemTouchHelperAdapter {
    private List<Book> books;
    private RecyclerView.Adapter mAdapter;
    private static IndexSourceList list;

    public static IndexSourceList getInstance() {
        if (list == null)
            list = new IndexSourceList();
        return list;
    }

    private IndexSourceList() {
        books = DataSupport.order("order").find(Book.class);
        if (books == null)
            books = new ArrayList<>();
    }


    public Book get(int position) {
        return books.get(position);
    }

    public int size() {
        return books.size();
    }

    /**
     * 添加时，如果包含该路径，那么就是已经添加过的
     *
     * @param book 是否包含这个path
     * @return 存在为真
     */
    boolean isPathExist(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getPath().equals(book.getPath()))
                return true;
        }
        return false;
    }

    /**
     * 添加对象
     *
     * @param book 需要保存的书籍<br />
     *             保存设定次序为现在有的数据，存储到数据库，保存在列表中，应该没有错误
     */
    void add(Book book) {
        book.setOrder(books.size());
        book.save();
        books.add(book);
        mAdapter.notifyItemInserted(books.size() - 1);
    }

    /**
     * 删除真实存在的某个对象
     *
     * @param position 在列表中的位置
     */
    public void delete(int position) {
        DataSupport.delete(Book.class, books.get(position).getId());
        mAdapter.notifyItemRemoved(position);
        books.remove(position);
    }

    public void setmAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        // TODO: 2017/10/19 检查更改成功了没
        books.get(fromPosition).setOrder(toPosition);
        Book moved = books.remove(fromPosition);
        //删除之后应该是从fromPosition -> toPosition-1
        for (int i = fromPosition; i < toPosition; i++) {
            Book cur = books.get(i);
            cur.setOrder(i - 1);
            cur.update(cur.getId());
        }
        books.add(toPosition, moved);
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}
