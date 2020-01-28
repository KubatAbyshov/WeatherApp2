package com.example.weatherapp.ui.onboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weatherapp.R;

import java.util.ArrayList;


public class OnBoardAdapter extends PagerAdapter {

    private ArrayList<OnBoardEntity> resource;

    ImageView imageView;
    TextView textView;

    public OnBoardAdapter(ArrayList<OnBoardEntity> resource) {
        this.resource = resource;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_onboard , container , false);

        imageView = view.findViewById(R.id.imgOnBoard);
        textView = view.findViewById(R.id.onboard_text);

        imageView.setImageResource(resource.get(position).getImg());
//        textView.setText(resource.get(position).getTitle());

        container.addView(view);

        return view;
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
