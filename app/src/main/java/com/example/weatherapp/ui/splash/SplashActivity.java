package com.example.weatherapp.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.example.weatherapp.R;
import com.example.weatherapp.data.PreferenceHelper;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;
import com.example.weatherapp.data.internet.RetrofitBuilder;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.main.MainActivity;
import com.example.weatherapp.ui.onboard.OnBoardActivity;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.weatherapp.BuildConfig.API_KEY;
import static com.example.weatherapp.ui.main.MainActivity.WEATHER_DATA;

public class SplashActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initLaunch();
        fetchForecast();
    }

    private void fetchForecast(){

        RetrofitBuilder.getService()
                .getForecast("Bishkek", API_KEY, "metric","ru")
                .enqueue(new Callback<ForecastEntity>() {
                    @Override
                    public void onResponse(Call<ForecastEntity> call, Response<ForecastEntity> response) {
                       if (response.isSuccessful() && response.body() != null){
                           start(response.body());
                           finish();
                       }
                        Log.d("onResponse: ", "sadasdasdsa");

                    }

                    @Override
                    public void onFailure(Call<ForecastEntity> call, Throwable t) {
                        Log.d("asdasdasd ", "onFailure");

                    }
                });

    }
    @Override
    protected int getViewLayout() {
        return R.layout.activity_splash;
    }

    public void start(ForecastEntity forecastEntity) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(WEATHER_DATA, forecastEntity);
        startActivity(intent);


    }
    


//    private void initLaunch() {
//        if (PreferenceHelper.getIsNotFirstLaunch()) {
//            MainActivity.start(this);
//        } else {
//            OnBoardActivity.start(this);
//            PreferenceHelper.setIsFirstLaunch();
//        }
//
//        finish();
//    }
}
