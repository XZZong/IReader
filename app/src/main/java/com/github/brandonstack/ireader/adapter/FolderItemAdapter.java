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
 * Created by 22693 on 2017/10/17.
 */

public class FolderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * onCreateViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_FOLDER.ordinal())
            return new FolderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scan_folder_item, parent, false));
        return new FileViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_file_item, parent, false));
    }

    /**
     * onBindViewHolder
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == ITEM_TYPE.ITEM_FILE.ordinal()) {
            ((FileViewHolder) holder).mCheckBox.setText(books.get(position).getName());
        } else {
            ((FolderViewHolder) holder).mTextView.setText(books.get(position).getFolder());
        }
    }

    /**
     * Item types
     * ENUM: Item Types
     */
    private enum ITEM_TYPE {
        ITEM_FILE,
        ITEM_FOLDER
    }

    @Override
    public int getItemViewType(int position) {

        if (books.get(position).getName() == null ||
                books.get(position).getName().equals(""))
            return ITEM_TYPE.ITEM_FOLDER.ordinal();
        return ITEM_TYPE.ITEM_FILE.ordinal();
    }

    /**
     * ViewHolders
     */
    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_scan_folder)
        TextView mTextView;

        FolderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_file_item)
        CheckBox mCheckBox;

        FileViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    private List<Book> books;
    private FolderSourceList list;

    public FolderItemAdapter() {
        list = FolderSourceList.getInstance();
        this.books = list.getBooks();
    }

    public int getItemCount() {
        return books == null ? 0 : books.size();
    }
}
