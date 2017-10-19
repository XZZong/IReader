package com.github.brandonstack.ireader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.brandonstack.ireader.R;
import com.github.brandonstack.ireader.entity.Book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 22693 on 2017/10/17.
 */

public class FolderItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * key:folder
     * value: files:book
     * 声明周期: 建立开始，add添加，到convert清空
     */
    private Map<String, List<Book>> lib;

    /**
     * book -- source
     * 声明周期: 建立初始化，convert添加，添加到书柜，清空
     */
    private List<Book> books;
    // TODO: 2017/10/19 检查lib book是否工作正常
    /**
     * 是否选中
     */
    private boolean[] checked;
    private BookshelfAdapter bookshelfAdapter;

    /**
     * 添加书本函数
     * 存在列表中忽略不计
     * 要不就根据key来进行添加
     */
    public void add(Book book) {
        //列表中是否存在
        if (!bookshelfAdapter.isPathExist(book)) {
            //如果列表不存在
            if (!lib.containsKey(book.getFolder()))
                lib.put(book.getFolder(), new ArrayList<Book>());
            //添加进去
            lib.get(book.getFolder()).add(book);
        }
    }

    /**
     * CheckBox跟踪
     * @param position 第几个
     * @param isChecked 是否选中
     */
    public void check(int position, boolean isChecked) {
        checked[position] = isChecked;
    }

    /**
     * 返回adapter函数的数据源
     */
    public void convert() {
        for (Map.Entry<String, List<Book>> entry : lib.entrySet()) {
            books.add(setBook(entry.getKey()));
            books.addAll(entry.getValue());
        }
        // TODO: 2017/10/19 检查lib是否清空了
        lib.clear();
        checked = new boolean[books.size()];
    }
    /**
     * type用的
     * @param folder 文件夹
     * @return 只包含文件夹的一个book
     */
    private Book setBook(String folder) {
        Book book = new Book();
        book.setFolder(folder);
        return book;
    }

    /**
     * 添加到另一个list之中
     * 清空自己的list
     */
    public void addToBookShelf() {
        for (int i = 0; i < checked.length; i++) {
            if (checked[i])
                bookshelfAdapter.add(books.get(i));
        }
        books.removeAll(books);
    }


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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = holder.getItemViewType();
        if (type == ITEM_TYPE.ITEM_FILE.ordinal()) {
            ((FileViewHolder) holder).mCheckBox.setText(books.get(position).getName());
            ((FileViewHolder) holder).mCheckBox
                    .setOnCheckedChangeListener(
                            new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    check(position, isChecked);
                                }
                            });
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


    public FolderItemAdapter() {
        lib = new LinkedHashMap<>();
        books = new ArrayList<>();
    }

    public void setBookshelfAdapter(BookshelfAdapter bookshelfAdapter) {
        this.bookshelfAdapter = bookshelfAdapter;
    }

    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

}
