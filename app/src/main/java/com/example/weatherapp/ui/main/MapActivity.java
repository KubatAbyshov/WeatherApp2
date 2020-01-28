package com.example.weatherapp.ui.main;

import android.os.Bundle;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.base.BaseMapActivity;

public class MapActivity extends BaseMapActivity {

    @Override
    protected int getViewLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
