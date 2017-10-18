package com.github.brandonstack.ireader;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.brandonstack.ireader.activity.BaseView;
import com.github.brandonstack.ireader.activity.ScanActivity;
import com.github.brandonstack.ireader.adapter.BookShelfSourceList;
import com.github.brandonstack.ireader.adapter.BookshelfAdapter;

import butterknife.BindView;

public class MainActivity extends BaseView
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab1)
    FloatingActionButton fab1;
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
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
//        Log.e(this.getLocalClassName(), "listener init finished");
    }


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
    protected void havePermission(int requestCode) {
    }
}
