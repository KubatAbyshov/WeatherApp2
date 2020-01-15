package com.example.weatherapp.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.CurrentWeather;
import com.example.weatherapp.data.internet.RetrofitBuilder;
import com.example.weatherapp.ui.base.BaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.text.DateFormat.getInstance;

public class MainActivity extends BaseActivity {

    Date currentDate = new Date();


    @BindView(R.id.clouds)
    ImageView clouds;

    @BindView(R.id.btn_weather)
    Button button;

    @BindView(R.id.temp_now)
    TextView tempNow;
    @BindView(R.id.temp_now_clouds)
    TextView tempNowClouds;

    @BindView(R.id.city)
    EditText editText;

    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.months)
    TextView months;
    @BindView(R.id.years)
    TextView years;

    @BindView(R.id.city_and_state)
    TextView city_and_state;

    @BindView(R.id.temp_today_max)
    TextView tempTodayMax;

    @BindView(R.id.temp_today_min)
    TextView tempTodayMin;

    @BindView(R.id.wind_text)
    TextView wind;

    @BindView(R.id.pressure_text)
    TextView pressure;

    @BindView(R.id.humidity_text)
    TextView humidity;

    @BindView(R.id.cloudiness_text)
    TextView cloudiness;

    @BindView(R.id.sunrise_text)
    TextView sunrise;

    @BindView(R.id.sunset_text)
    TextView sunset;


    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        date();
        fetchCurrentWeather("Bishkek");

    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    private void initViews() {

        button.setOnClickListener(v -> fetchCurrentWeather(editText.getText().toString().trim()));
        hideKeyboard();
    }

    private void fetchCurrentWeather(String city) {
        RetrofitBuilder.getService()
                .fetchtCurrentWeather(editText.getText().toString().trim(),
                        "4d63c1acf9a085448b23971128e5eddd", "metric")
                .enqueue(new Callback<CurrentWeather>() {

                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            fillViews(response.body());

                            Glide.with(MainActivity.this).load("https://openweathermap.org/img/wn/" + response.body().
                                    getWeather().get(0).getIcon() + "@2x.png").centerCrop().into(clouds);

                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void fillViews(CurrentWeather weather) {

        tempNow.setText(weather.getMain().getTemp().toString() + "°C");
        tempNowClouds.setText(weather.getWeather().get(0).getDescription());
        city_and_state.setText(weather.getName() + ", " + weather.getSys().getCountry());
        tempTodayMax.setText(weather.getMain().getTempMax().toString());
        tempTodayMin.setText(weather.getMain().getTempMin().toString());
        wind.setText(convertDegreeToCardinalDirection(weather.getWind().getDeg()) + " " + weather.getWind().getSpeed().toString() + "m/s");
        pressure.setText(weather.getMain().getPressure().toString() + "mb");
        humidity.setText(weather.getMain().getHumidity().toString() + "%");
        cloudiness.setText(weather.getClouds().getAll().toString() + "%");
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mmaa");
        Date dateSunrise = new Date(weather.getSys().getSunrise() * 1000);
        Date dateSunset = new Date(weather.getSys().getSunset() * 1000);
        sunset.setText(dateFormat.format(dateSunrise.getTime()));
        sunrise.setText(dateFormat.format(dateSunset.getTime()));




    }

    public void date() {

        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        DateFormat monthsFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        DateFormat yearsFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        String monthsText = monthsFormat.format(currentDate);
        String yearsText = yearsFormat.format(currentDate);
        date.setText(dateText);
        months.setText(monthsText);
        years.setText(yearsText);

    }

    public String convertDegreeToCardinalDirection(int directionInDegrees) {
        String cardinalDirection = null;
        if ((directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)) {
            cardinalDirection = "N";
        } else if ((directionInDegrees >= 11.25) && (directionInDegrees <= 33.75)) {
            cardinalDirection = "NNE";
        } else if ((directionInDegrees >= 33.75) && (directionInDegrees <= 56.25)) {
            cardinalDirection = "NE";
        } else if ((directionInDegrees >= 56.25) && (directionInDegrees <= 78.75)) {
            cardinalDirection = "ENE";
        } else if ((directionInDegrees >= 78.75) && (directionInDegrees <= 101.25)) {
            cardinalDirection = "E";
        } else if ((directionInDegrees >= 101.25) && (directionInDegrees <= 123.75)) {
            cardinalDirection = "ESE";
        } else if ((directionInDegrees >= 123.75) && (directionInDegrees <= 146.25)) {
            cardinalDirection = "SE";
        } else if ((directionInDegrees >= 146.25) && (directionInDegrees <= 168.75)) {
            cardinalDirection = "SSE";
        } else if ((directionInDegrees >= 168.75) && (directionInDegrees <= 191.25)) {
            cardinalDirection = "S";
        } else if ((directionInDegrees >= 191.25) && (directionInDegrees <= 213.75)) {
            cardinalDirection = "SSW";
        } else if ((directionInDegrees >= 213.75) && (directionInDegrees <= 236.25)) {
            cardinalDirection = "SW";
        } else if ((directionInDegrees >= 236.25) && (directionInDegrees <= 258.75)) {
            cardinalDirection = "WSW";
        } else if ((directionInDegrees >= 258.75) && (directionInDegrees <= 281.25)) {
            cardinalDirection = "W";
        } else if ((directionInDegrees >= 281.25) && (directionInDegrees <= 303.75)) {
            cardinalDirection = "WNW";
        } else if ((directionInDegrees >= 303.75) && (directionInDegrees <= 326.25)) {
            cardinalDirection = "NW";
        } else if ((directionInDegrees >= 326.25) && (directionInDegrees <= 348.75)) {
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }


    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
