package com.example.weatherapp.data.internet;

import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.weatherapp.data.internet.APIEndpoints.CURRENT_WEATHER;
import static com.example.weatherapp.data.internet.APIEndpoints.FORECAST;

public interface RetrofitService {

    @GET(CURRENT_WEATHER)
    Call<CurrentWeather> fetchCurrentWeather(@Query("q") String city,
                                              @Query("appid") String appId,
                                              @Query("units") String metric,
                                              @Query("lang")  String ru);

    @GET(FORECAST)
    Call<ForecastEntity> getForecast(@Query("q") String city,
                                             @Query("appid") String appId,
                                             @Query("units") String metric,
                                             @Query("lang")  String ru);

    @GET(CURRENT_WEATHER)
    Call<CurrentWeather> coordCurrentWeather(@Query("lat") double lat,
                                             @Query("lon") double lon,
                                             @Query("units") String format,
                                             @Query("appid") String key,
                                             @Query("lang")  String ru);
    @GET(FORECAST)
    Call<ForecastEntity> coordForCastWeather(@Query("lat") double lat,
                                             @Query("lon") double lon,
                                             @Query("units") String format,
                                             @Query("appid") String key,
                                             @Query("lang")  String ru);
}
