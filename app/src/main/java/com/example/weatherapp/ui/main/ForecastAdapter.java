package com.example.weatherapp.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private ArrayList<CurrentWeather> forecastWeatherList = new ArrayList<>();


    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    public void update(ArrayList<CurrentWeather> forecastWeatherList) {
        this.forecastWeatherList = forecastWeatherList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.bind(forecastWeatherList.get(position).getDateTimeForCast(),
                forecastWeatherList.get(position).getMain().getTempMax().toString() + "°C",
                forecastWeatherList.get(position).getMain().getTempMin().toString() + "°C",
                forecastWeatherList.get(position).getWeather().get(0).getIcon());
    }

    @Override
    public int getItemCount() {
        return forecastWeatherList.size();
    }
}
