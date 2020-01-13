package com.example.weatherapp.ui.splash;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.weatherapp.R;
import com.example.weatherapp.data.PreferenceHelper;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.main.MainActivity;
import com.example.weatherapp.ui.onboard.OnBoardActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {


    @Override
    protected int getViewLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLaunch();
    }

    private void initLaunch() {
        if (PreferenceHelper.getIsNotFirstLaunch()) {
            MainActivity.start(this);
        } else {
            OnBoardActivity.start(this);
            PreferenceHelper.setIsFirstLaunch();
        }

        finish();
    }
}
