package com.github.brandonstack.ireader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 22693 on 2017/10/17.
 */

public class FolderItemAdapter extends RecyclerView.Adapter<FolderItemAdapter.MyViewHolder> {
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_folder_name)
        TextView mTextView;
        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private Map<String, List<Book>> books;

    public FolderItemAdapter(Map<String, List<Book>> books) {
        this.books = books;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String key = ((String) books.keySet().toArray()[position]);
        HashSet<Integer> i = new HashSet<>();
        i.toArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_files, parent, false));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
