package com.example.weatherapp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.data.entity.current.CurrentWeather;
import com.example.weatherapp.data.entity.forecast.ForecastEntity;
import com.example.weatherapp.data.internet.RetrofitBuilder;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.onboard.OnBoardActivity;
import com.example.weatherapp.ui.splash.SplashActivity;
import com.example.weatherapp.utils.DateUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.xml.sax.helpers.ParserAdapter;

import java.security.acl.Permission;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.weatherapp.BuildConfig.API_KEY;
import static com.example.weatherapp.BuildConfig.ICON_SIZE;
import static com.example.weatherapp.BuildConfig.ICON_URL;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE = 1001;

    public static String WEATHER_DATA = "weather";
    private CurrentWeather weather_data;
    private RecyclerView recyclerView;
    ForecastAdapter adapter;

    FloatingActionButton fabStart, fabStop;

    private static final String IS_SERVICE_ACTIVE = "isActivated";

    private FusedLocationProviderClient fusedLocationProviderClient;
    String[] permissions = new String[2];

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

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

    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.months)
    TextView months;
    @BindView(R.id.years)
    TextView years;

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
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        initViews();
        getDate();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;


        if (ContextCompat.checkSelfPermission(this, permissions[0])
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1])
                        == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            double latitude = locationResult.getLastLocation().getLatitude();
                            double longitude = locationResult.getLastLocation().getLongitude();
                            coordCurrentWeather(latitude, longitude);
                            coordfetchForecast(latitude, longitude);
                        }
                    }, getMainLooper());

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_CODE);
                getCurrentCoordinate();
            }
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            for (int results : grantResults) {
                if (results == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                }
            }
        }
    }


    void getLastLocation() {

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        callPermissions();
                        getCurrentCoordinate();
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                startActivity(new Intent(this, MapActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void actionService(boolean isActivated) {
        Intent intent = new Intent(this, MyForegroundService.class);
        intent.putExtra(IS_SERVICE_ACTIVE, isActivated);
        startService(intent);
    }


    private void initViews() {

        fabStart = findViewById(R.id.fab_start);
        fabStop = findViewById(R.id.fab_stop);

        fabStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                actionService(true);
                fabStart.setVisibility(GONE);
                fabStop.setVisibility(VISIBLE);
            }
        });
        fabStop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                actionService(false);
                fabStart.setVisibility(VISIBLE);
                fabStop.setVisibility(GONE);
            }
        });


        button.setOnClickListener(v -> {
            fetchCurrentWeather(editText.getText().toString());
            fetchForecast(editText.getText().toString());
        });


    }


    private void initRecycler(ArrayList<CurrentWeather> list) {
        Intent intent = getIntent();
        ForecastEntity forecastEntity = (ForecastEntity) intent.getSerializableExtra(WEATHER_DATA);
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ForecastAdapter();
        recyclerView.setAdapter(adapter);
        adapter.update(list);
    }


    private void fetchCurrentWeather(String city) {
        RetrofitBuilder.getService()
                .fetchCurrentWeather(editText.getText().toString(),
                        API_KEY, "metric", "ru")
                .enqueue(new Callback<CurrentWeather>() {

                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            fillViews(response.body());

                            Glide.with(MainActivity.this).load(ICON_URL + response.body().
                                    getWeather().get(0).getIcon() + ICON_SIZE).into(clouds);

                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void coordCurrentWeather(double latitude, double longitude) {
        RetrofitBuilder
                .getService()
                .coordCurrentWeather(latitude, longitude,
                        API_KEY, "metric", "ru")
                .enqueue(new Callback<CurrentWeather>() {

                    @Override
                    public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            weather_data = response.body();
                            Glide.with(MainActivity.this).load(ICON_URL + response.body().
                                    getWeather().get(0).getIcon() + ICON_SIZE).into(clouds);

                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeather> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void fetchForecast(String city) {
        RetrofitBuilder.getService()
                .getForecast(editText.getText().toString(),
                        API_KEY, "metric", "ru")
                .enqueue(new Callback<ForecastEntity>() {
                    @Override
                    public void onResponse(Call<ForecastEntity> call, Response<ForecastEntity> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            initRecycler(response.body().getForecastWeatherList());

                        }

                    }

                    @Override
                    public void onFailure(Call<ForecastEntity> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ошибка получения данных за неделю", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void coordfetchForecast(double latitude, double longitude) {
        RetrofitBuilder.getService()
                .coordForCastWeather(latitude, longitude,
                        API_KEY, "metric", "ru")
                .enqueue(new Callback<ForecastEntity>() {
                    @Override
                    public void onResponse(Call<ForecastEntity> call, Response<ForecastEntity> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.update(response.body().getForecastWeatherList());
                        }

                    }

                    @Override
                    public void onFailure(Call<ForecastEntity> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ошибка получения данных за неделю", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void getCurrentCoordinate() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            double latitude = locationResult.getLastLocation().getLatitude();
                            double longitude = locationResult.getLastLocation().getLongitude();
                            coordCurrentWeather(latitude, longitude);
                            coordfetchForecast(latitude, longitude);
                        }
                    }, getMainLooper());
        } else {
            Toast.makeText(this, "Ошибка получения координат", Toast.LENGTH_SHORT).show();
        }
    }

    public void callPermissions() {
        Permissions.check(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, "Нужен доступ к местоположению",
                new Permissions.Options().setSettingsDialogTitle("Внимание!!!").setRationaleDialogTitle("Доступ к местоположению"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        getCurrentCoordinate();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                        callPermissions();
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


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        sunset.setText(dateFormat.format(weather.getSys().getSunset() * 1000));
        sunrise.setText(dateFormat.format(weather.getSys().getSunrise() * 1000));


    }

    public void getDate() {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        DateFormat monthsFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        DateFormat yearsFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        date.setText(dateFormat.format(currentDate));
        months.setText(monthsFormat.format(currentDate));
        years.setText(yearsFormat.format(currentDate));

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
}
