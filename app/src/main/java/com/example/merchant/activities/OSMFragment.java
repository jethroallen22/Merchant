package com.example.merchant.activities;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.merchant.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.Map;


public class OSMFragment extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MapView mapView;
    private Button btn_confirm_marker;
    int m=0;
    double rlat;
    double rlong;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_osm);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

//        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
//        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid");
//        osmConf.setOsmdroidBasePath(basePath);
//        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
//        osmConf.setOsmdroidTileCache(tileCache);

        btn_confirm_marker = findViewById(R.id.btn_confirm_marker);
        mapView = findViewById(R.id.mapView);
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
                    rlat = p.getLatitude();
                    rlong = p.getLongitude();
                } else if (m==1){
                    Log.d("Marker", "INSIDE ELSE");
                    mapView.getOverlays().remove(1);
                    marker.setPosition(new GeoPoint(p.getLatitude(), p.getLongitude()));
                    marker.setTitle("Store");
                    Log.d("Marker", marker.getTitle() + " Lat:"+p.getLatitude()+"+Long:"+p.getLongitude());
                    mapView.getOverlays().add(marker);
                    rlat = p.getLatitude();
                    rlong = p.getLongitude();
                }
                Log.d("Lat and Long", "Lat:"+p.getLatitude()+"+Long:"+p.getLongitude());
                Toast.makeText(getApplicationContext(), String.valueOf(p.getLatitude()) +" "+ String.valueOf(p.getLongitude()) , Toast.LENGTH_LONG);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));

        btn_confirm_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Lat and Long", "Lat:"+rlat+"+Long:"+rlong);
            }
        });
    }


}
