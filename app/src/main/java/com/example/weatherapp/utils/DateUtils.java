package com.example.weatherapp.utils;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String forCastDate(String s) throws ParseException {

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dt.parse(s);

        SimpleDateFormat outDt = new SimpleDateFormat("dd.MMM");
        String parseDate = outDt.format(date);
        return parseDate;
    }

}
