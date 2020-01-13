package com.example.weatherapp.ui.onboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.OnBoardEntity;

import java.util.ArrayList;


public class OnBoardAdapter extends PagerAdapter {
    private Context context;

    private ArrayList<OnBoardEntity> resource;

    public OnBoardAdapter(ArrayList<OnBoardEntity> resource) {
        this.resource = resource;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_onboard, null);
        initView(view, container, position);
        return view;
    }

    private void initView(View view, ViewGroup container, int position) {
        ImageView imageView = view.findViewById(R.id.imgOnBoard);
        imageView.setImageDrawable(container.getContext().getResources().getDrawable(resource.get(position).getImg()));
        container.addView(view);
    }

    @Override
    public int getCount() {
        return resource.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
