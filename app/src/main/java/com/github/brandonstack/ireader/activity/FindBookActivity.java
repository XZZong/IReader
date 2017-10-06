package com.github.brandonstack.ireader.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.R;

import butterknife.BindView;

public class FindBookActivity extends BaseView {
    @BindView(R.id.insert_edit)
    EditText mEditText;
    @BindView(R.id.insert_post)
    Button mButton;

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book newBook = new Book();
                String name = mEditText.getText().toString();
                newBook.setName(name);
                Toast.makeText(getBaseContext(),name,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_find_book;
    }
}
