package com.example.weatherapp.ui.test;

import android.widget.TextView;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class TestFragment extends BaseFragment {

    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.tvTvMain)
    public void textClicked(TextView textView){
        textView.setText("asdsadsad");
    }
}
