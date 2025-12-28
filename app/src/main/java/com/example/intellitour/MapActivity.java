package com.example.intellitour;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.maplibre.android.MapLibre;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.location.LocationComponent;
import org.maplibre.android.location.LocationComponentActivationOptions;
import org.maplibre.android.location.modes.RenderMode;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.Style;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MapView mapView;
    private MapLibreMap map;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLibre.getInstance(this);
        setContentView(R.layout.activity_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String geoapifyApiKey = getGeoapifyApiKey();

        if (geoapifyApiKey == null || geoapifyApiKey.equals("YOUR_GEOAPIFY_API_KEY_HERE")) {
            Toast.makeText(this, "Geoapify API Key is missing in AndroidManifest.xml!", Toast.LENGTH_LONG).show();
            return;
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapLibreMap -> {
            map = mapLibreMap;
            String styleUrl = "https://maps.geoapify.com/v1/styles/osm-bright/style.json?apiKey=" + geoapifyApiKey;
            map.setStyle(styleUrl, style -> {
                Log.d(TAG, "Map style loaded successfully.");
                enableLocationFeatures(style);
            });
        });

        FloatingActionButton fabMyLocation = findViewById(R.id.fab_my_location);
        fabMyLocation.setOnClickListener(v -> zoomToCurrentLocation());
    }

    private String getGeoapifyApiKey() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("com.geoapify.API_KEY");
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data from manifest", e);
            return null;
        }
    }

    private void enableLocationFeatures(@NonNull Style loadedMapStyle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Activate the LocationComponent to show the blue dot
            LocationComponent locationComponent = map.getLocationComponent();
            LocationComponentActivationOptions activationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle).build();
            locationComponent.activateLocationComponent(activationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setRenderMode(RenderMode.COMPASS);

            // Then, get a one-time location update to zoom the camera
            zoomToCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void zoomToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null && map != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition position = new CameraPosition.Builder()
                        .target(userLocation)
                        .zoom(16) // Zoom in close
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1500);
            } else {
                Toast.makeText(this, "Could not get current location. Please ensure location is enabled.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (map != null && map.getStyle() != null) {
                    enableLocationFeatures(map.getStyle());
                }
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // --- MapView Lifecycle Methods ---
    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) mapView.onSaveInstanceState(outState);
    }
}