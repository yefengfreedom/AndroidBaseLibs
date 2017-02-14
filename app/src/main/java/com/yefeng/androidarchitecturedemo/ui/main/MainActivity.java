package com.yefeng.androidarchitecturedemo.ui.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.freedom.yefeng.yfrecyclerview.YfListInterface;
import com.freedom.yefeng.yfrecyclerview.YfListMode;
import com.freedom.yefeng.yfrecyclerview.YfListRecyclerView;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.data.model.book.Book;
import com.yefeng.androidarchitecturedemo.data.source.book.BookRepository;
import com.yefeng.androidarchitecturedemo.data.source.book.local.BookLocalDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.memory.BookMemoryDataSource;
import com.yefeng.androidarchitecturedemo.data.source.book.remote.BookRemoteDataSource;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * ui main view
 */
public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    private YfListRecyclerView mList;
    private MainAdapter mAdapter;

    private SwipeRefreshLayout mSwipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        // init presenter
        BookRepository bookRepository = BookRepository.getInstance(
                new BookRemoteDataSource(),
                new BookLocalDataSource(),
                new BookMemoryDataSource()
        );
        mPresenter = new MainPresenter(bookRepository, this);

        //init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init swipe refresh layout
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(() -> mPresenter.loadBooks(true));

        mList = (YfListRecyclerView) findViewById(R.id.recycler);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initAdapter();
        mList.setAdapter(mAdapter);
        mList.setDivider(R.mipmap.divider);
    }

    private void initAdapter() {
        mAdapter = new MainAdapter(null);
        mAdapter.setOnItemClickListener(new YfListInterface.OnItemClickListener<Book>() {
            @Override
            public void onItemClick(View view, Book book) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("delete book: " + book.getTitle() + " ?")
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("delete", (dialog, which) -> mPresenter.deleteBook(String.valueOf(book.getId())))
                        .create()
                        .show();
            }
        });
        mAdapter.setOnEmptyViewClickListener(view -> showToast("click empty view"));
        mAdapter.setOnErrorViewClickListener(view -> showToast("click error view"));
    }

    private void showToast(String msg) {
        Snackbar.make(mList, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public void onLoading() {
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    public void onLoadOk(ArrayList<Book> books) {
        mAdapter.setData(books);
    }

    @Override
    public void onLoadError(String msg) {
        mSwipeLayout.setRefreshing(false);
        mAdapter.changeMode(YfListMode.MODE_ERROR);
        showToast(msg);
    }

    @Override
    public void onLoadFinish() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        Timber.d("setPresenter()");
    }
}
