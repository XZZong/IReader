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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 22693 on 2017/10/6.
 */

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.ViewHolder> {
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

    private List<Book> books;
    private Context context;

    public BookshelfAdapter(List<Book> books,Context context) {
        this.books = books;
        this.context = context;
    }
    // TODO: 2017/10/17 这个抽象的还不是很合理，正确的应该getlist也去掉，直接从内部得到，这样相当于数据直接暴露

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
                intent.putExtra("book",books.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
