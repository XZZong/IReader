package com.github.brandonstack.ireader.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Adapter holder",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            });
        }
    }

    private List<Book> books;

    public BookshelfAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_text_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(books.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
