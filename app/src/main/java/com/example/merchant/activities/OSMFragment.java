package com.example.merchant.activities;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.merchant.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.IMapView;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.Map;


public class OSMFragment extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MapView mapView;
    private IMapController mapController;
    int m=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_osm);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        mapView = findViewById(R.id.mapView);
//        mapView.setTileSource(new OpenStreetMapTileSource());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setCenter(new GeoPoint(14.56610, 120.99244));
        mapView.getController().setZoom(20);

        mapView.invalidate();

        mapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Marker marker = new Marker(mapView);
                if(m==0){
                    marker.setPosition(new GeoPoint(p.getLatitude(), p.getLongitude()));
                    marker.setTitle("Store");
                    Log.d("Marker", marker.getTitle() + " Lat:"+p.getLatitude()+"+Long:"+p.getLongitude());
                    mapView.getOverlays().add(marker);
                    m++;
                } else {
                    Log.d("Marker", "INSIDE ELSE");

//                    marker.
                    mapView.getOverlays().remove(1);
                    marker.setPosition(new GeoPoint(p.getLatitude(), p.getLongitude()));
                    marker.setTitle("Store");
                    Log.d("Marker", marker.getTitle() + " Lat:"+p.getLatitude()+"+Long:"+p.getLongitude());
                    mapView.getOverlays().add(marker);
                    m--;
                    Log.d("M", String.valueOf(m));
                }

//                }

                Log.d("Lat and Long", "Lat:"+p.getLatitude()+"+Long:"+p.getLongitude());
                Toast.makeText(getApplicationContext(), String.valueOf(p.getLatitude()) +" "+ String.valueOf(p.getLongitude()) , Toast.LENGTH_LONG);
//                Toast.makeText(getApplicationContext(), String.valueOf(p.getLongitude()), Toast.LENGTH_LONG);
//                et_long.setHint(String.valueOf(p.getLongitude()));
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));
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
