package com.yefeng.androidarchitecturedemo.ui.menu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.freedom.yefeng.yfrecyclerview.HiInterface;
import com.freedom.yefeng.yfrecyclerview.HiRecyclerView;
import com.yefeng.androidarchitecturedemo.R;
import com.yefeng.androidarchitecturedemo.ui.clean.MainCleanActivity;
import com.yefeng.androidarchitecturedemo.ui.mvp.MainActivity;
import com.yefeng.support.base.AppInfo;

import java.util.ArrayList;

/**
 * Created by yefeng on 21/02/2017.
 */

public class MenuActivity extends AppCompatActivity {
    ImageView mIvShow;
    Toolbar mTb;
    CollapsingToolbarLayout mCtl;
    CoordinatorLayout mCl;
    HiRecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mIvShow = (ImageView) findViewById(R.id.iv_show);
        mTb = (Toolbar) findViewById(R.id.tb);
        mCtl = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        mCl = (CoordinatorLayout) findViewById(R.id.cl);
        mList = (HiRecyclerView) findViewById(R.id.rv);
        initToolbar();
        init();
    }

    private void initToolbar() {
        setSupportActionBar(mTb);
        final ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.setHomeAsUpIndicator(R.drawable.ic_nav_story);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mTb.setTitleTextColor(getResources().getColor(R.color.white));
        mCtl.setTitle("Android Architecture Demo");
        mCtl.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        int height = AppInfo.sWidth / 4 * 3;
        CollapsingToolbarLayout.LayoutParams lp = new CollapsingToolbarLayout.LayoutParams(AppInfo.sWidth, height);
        mIvShow.setLayoutParams(lp);
    }

    private void init() {
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("Android Mvp Architecture Demo");
        actions.add("Android Clean Architecture Demo");
        actions.add("Android Mvvm Architecture Demo");
        MenuAdapter adapter = new MenuAdapter(actions);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mList.setAdapter(adapter);
        adapter.setOnItemClickListener(new HiInterface.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object o) {
                if ("Android Clean Architecture Demo".equals(o.toString())) {
                    startActivity(new Intent(MenuActivity.this, MainCleanActivity.class));
                } else {
                    startActivity(new Intent(MenuActivity.this, MainActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Uri uri = Uri.parse("https://github.com/yefengfreedom");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
