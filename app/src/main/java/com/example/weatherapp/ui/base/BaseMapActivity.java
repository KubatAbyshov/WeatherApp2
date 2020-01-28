package com.example.weatherapp.ui.base;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.weatherapp.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin;

import java.util.List;

public abstract class BaseMapActivity extends BaseActivity implements PermissionsListener {

    protected MapView mapView;
    protected MapboxMap map;

    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, "pk.eyJ1Ijoicm9qc2FzaGEiLCJhIjoiY2p6MTB6ZjI2MGd4aTNlbXl4Mzd5YTV1dSJ9.uf5mvNIAZZEg4UpGGd5w7Q");
        super.onCreate(savedInstanceState);
        initViews();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        map = mapboxMap;
                        BuildingPlugin buildingPlugin = new BuildingPlugin(mapView, mapboxMap, style);
                        buildingPlugin.setVisibility(true);

                        enableLocationComponent(style);
                    }
                });
            }
        });
    }

    private void enableLocationComponent(Style style){
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .build();

            locationComponent = map.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, style)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            Location location = locationComponent.getLastKnownLocation();

            if (location != null) {
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(18)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000);
            }




        } else {

            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initViews(){
        mapView = findViewById(R.id.mapView);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "dsfsafdadf", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "asdasdasd", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
