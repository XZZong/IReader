package com.github.brandonstack.ireader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FileItemAdapter.
 * Created by 22693 on 2017/10/17.
 */

public class FileItemAdapter extends RecyclerView.Adapter<FileItemAdapter.MyViewHolder> {
    private List<Book> books;

    public FileItemAdapter(List<Book> books) {
        this.books = books;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_file_item)
        CheckBox mCheckBox;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mCheckBox.setText(books.get(position).getName());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
