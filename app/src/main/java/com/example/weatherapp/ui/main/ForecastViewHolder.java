package com.example.weatherapp.ui.main;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;
import com.example.weatherapp.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.weatherapp.BuildConfig.ICON_SIZE;
import static com.example.weatherapp.BuildConfig.ICON_URL;

public class ForecastViewHolder extends RecyclerView.ViewHolder {


    private TextView tvMinTemp;
    private ImageView imageView;
    private TextView tvMaxTemp;
    private TextView tvDate;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
        tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
        imageView = itemView.findViewById(R.id.week_clouds);
        tvDate = itemView.findViewById(R.id.tvDate);
    }

    public void bind(String dt, String max, String min, String img) {

        try {
            tvDate.setText(DateUtils.forCastDate(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvMinTemp.setText(min);
        tvMaxTemp.setText(max);


        Glide.with(itemView).load(ICON_URL + img + ICON_SIZE).into(imageView);
    }

}
