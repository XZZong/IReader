package com.github.brandonstack.ireader;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.brandonstack.ireader.activity.BaseView;
import com.github.brandonstack.ireader.adapter.BookShelfSourceList;
import com.github.brandonstack.ireader.adapter.BookshelfAdapter;
import com.github.brandonstack.ireader.entity.Book;
import com.github.brandonstack.ireader.util.Content;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseView
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int READ_EXTERNAL_REQUEST_CODE = 10;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.bookshelf)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ActionBarDrawerToggle toggle;
    BookShelfSourceList bookShelfSourceList;
    private static final int READERFILES = 1;

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(mLayoutManager);


        bookShelfSourceList = BookShelfSourceList.getInstance();
        mAdapter = new BookshelfAdapter(bookShelfSourceList.getBooks());
        bookShelfSourceList.setAdapter(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, FindBookActivity.class);
//                MainActivity.this.startActivity(intent);
                //检查权限，如果有权限进行相应操作
                checkPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_REQUEST_CODE, "需要扫描储存卡的权限");
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
//        Log.e(this.getLocalClassName(), "listener init finished");
    }

    private void scanFiles() {
        showProgress(true, "Loading");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Content content = new Content(MainActivity.this);
                List<Book> books = content.queryFiles();
                Message msg = Message.obtain();
                msg.obj = books;
                msg.what = READERFILES;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<Book> books = ((List<Book>) msg.obj);
            for (Book book : books) {
                Log.e(MainActivity.this.getClass().getSimpleName(), book.getName());
            }
            hideProgress();
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bookShelfSourceList.refresh();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_REQUEST_CODE: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    havePermission(READ_EXTERNAL_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Permission Rejected", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    @Override
    protected void havePermission(int requestCode) {
        switch (requestCode) {
            case READ_EXTERNAL_REQUEST_CODE: {
                scanFiles();
            }
        }
    }
}
