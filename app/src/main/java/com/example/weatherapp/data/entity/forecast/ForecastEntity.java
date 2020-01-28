package com.example.weatherapp.data.entity.forecast;

import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ForecastEntity implements Serializable {

    private String cod;
    private int message;

    @SerializedName(value = "cnt", alternate = {"dsafasdf", "asdfasdfa", "sadfasdfas"})
    private int count;

    @SerializedName("list")
    private ArrayList<CurrentWeather> forecastWeatherList;


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CurrentWeather> getForecastWeatherList() {
        return forecastWeatherList;
    }

    public void setForecastWeatherList(ArrayList<CurrentWeather> forecastWeatherList) {
        this.forecastWeatherList = forecastWeatherList;
    }
}
