package com.example.weatherapp.ui.onboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.OnBoardEntity;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

public class OnBoardActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.btn_next)
    Button button;

    private Button btn_start;
    private Button btn_next;

    private int currentItem;


    @Override
    protected int getViewLayout() {
        return R.layout.activity_on_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        btn_next = findViewById(R.id.btn_next);
        init();
        setupClickListener();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.skip:
                MainActivity.start(this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_onboard_skip, menu);
        return true;
    }


    public static void start(Context context) {
        context.startActivity(new Intent(context, OnBoardActivity.class));
    }

    private ArrayList<OnBoardEntity> getResource() {
        ArrayList<OnBoardEntity> list = new ArrayList<>();
        list.add(new OnBoardEntity("В данном приложении вы можете учиться))", R.drawable.picture1));
        list.add(new OnBoardEntity("В данном приложении вы можете обновлять))", R.drawable.picture2));
        list.add(new OnBoardEntity("В данном приложении вы можете удалять", R.drawable.picture3));
        list.add(new OnBoardEntity("Спасибо что вы с нами))", R.drawable.onboard_page11));
        return list;
    }

    private void setupClickListener() {
        button.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() + 1));
        OnBoardAdapter onBoardAdapter = new OnBoardAdapter(getResource());
        pager.setAdapter(onBoardAdapter);
    }

    public void init() {

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pager.getCurrentItem() == 3) {
                    button.setText("Finish");
                    button.setOnClickListener(v -> {
                        MainActivity.start(OnBoardActivity.this);
                        finish();
                    });
                } else {
                    button.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void Click(View view) {
        pager.setCurrentItem(currentItem + 1);
    }

}
