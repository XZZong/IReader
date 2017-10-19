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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 22693 on 2017/10/6.
 */

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.ViewHolder> {

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

    IndexSourceList list;

    public BookshelfAdapter(Context context) {
        // TODO: 2017/10/19 检查query的结果是否符合
        this.context = context;
        list = IndexSourceList.getInstance();
        list.setmAdapter(this);
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
        holder.mTextView.setText(list.get(position).getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("book", list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
