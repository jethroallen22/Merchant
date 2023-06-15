package com.example.merchant.activities;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.merchant.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


    public class OSMFragment extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MapView mapView;
    private IMapController mapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_osm);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        mapView = findViewById(R.id.mapView);
//        mapView.setTileSource(new OpenStreetMapTileSource());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setCenter(new GeoPoint(51.0, 4.0));
        mapView.getController().setZoom(11);

        mapView.invalidate();
//        setContentView(R.layout.fragment_osm);
//
//        // Create a new MapView object and set the map's properties
//        mapView = (MapView) findViewById(R.id.mapView);
//        mapView.setTileSource(TileSourceFactory.MAPNIK);
//        mapView.setBuiltInZoomControls(true);
//
//        // Create a new OSMdroid object and set the map's properties
//        OSMdroid osmdroid = this;
//        osmdroid.setMapView(mapView);
//
//        // Add a marker to the map
//        Marker marker = new Marker(mapView);
//        marker.setPosition(new GeoPoint(51.5, -0.1));
//        marker.setTitle("London");
//        mapView.getOverlays().add(marker);
//
//        // Set the map's center to London
//        mapController = mapView.getController();
//        mapController.setCenter(new GeoPoint(51.5, -0.1));
//        mapController.setZoom(15);
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
}
