package com.github.brandonstack.ireader.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.brandonstack.ireader.activity.ReadActivity;
import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.util.Content;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 22693 on 2017/10/6.
 */

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Book> books;

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
        notifyItemInserted(books.size() - 1);
    }

    /**
     * 删除真实存在的某个对象
     *
     * @param position 在列表中的位置
     */
    public void delete(int position) {
        DataSupport.delete(Book.class, books.get(position).getId());
        notifyItemRemoved(position);
        books.remove(position);
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
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * ViewHolder
     * set TextView and get CardView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.book_item_text)
        public TextView mTextView;
        @BindView(R.id.book_item)
        public CardView mCardView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private Context context;

    public BookshelfAdapter(Context context) {
        // TODO: 2017/10/19 检查query的结果是否符合
        this.context = context;
        books = DataSupport.order("order").find(Book.class);
    }

    /**
     * 创建时调用
     *
     * @param parent   parent
     * @param viewType 没用呀
     * @return ViewHolder类
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_text_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(books.get(position).getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("book", books.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }
}
