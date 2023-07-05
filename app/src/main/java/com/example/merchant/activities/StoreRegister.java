package com.example.merchant.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.RealPathUtil;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.StoreModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreRegister extends AppCompatActivity {
    EditText name_text_input, description_text_input, location_text_input, category_text_input, start_time_text_input, end_time_text_input;
    Button btn_upload, btn_register_store;
    ImageView iv_store_img;

    String image, name, description, location, category;
    Float rating, popularity;
    int start_time;
    int end_time;
    String uname, email, number, password;
    private RequestQueue requestQueue1;
    int merchantId=0, m=0;

    String path, base64Image;
    Bitmap bitmap;

    private static String JSON_URL;
    private IPModel ipModel;
    private MapView mapView;
    double rlat, rlong;
    ChipGroup cg_store;
    List<String> category_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        getSupportActionBar().hide();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Initialize();

//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        category_spinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null){
            uname = intent.getStringExtra("Name");
            email = intent.getStringExtra("Email");
            number = intent.getStringExtra("Number");
            password = intent.getStringExtra("Password");
            Log.d("USER", "UN: " + uname);
        }
        requestQueue1 = Singleton.getsInstance(this).getRequestQueue();
        extractMerchantId(uname);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                        iv_store_img.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

//        //Category
//        category_list = new ArrayList<>();
//        category_list.add("American");
//        category_list.add("Chinese");
//        category_list.add("Filipino");
//        category_list.add("Japanese");
//        category_list.add("Thai");

//        for (String str : category_list) {
//            final Chip chip = new Chip(g);
//            chip.setText(str);
//            chip.setClickable(true);
//            chip.setChipBackgroundColorResource(R.color.chipDefault);
//            chip.setTextColor(getResources().getColor(R.color.black));
//
//            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    Log.d("OnCheckedChanged", (String) chip.getText());
//                }
//            });
////            chip.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    for (int i = 0; i < cg_product.getChildCount(); i++) {
////                        Chip currentChip = (Chip) cg_product.getChildAt(i);
////                        if (currentChip != chip) {
////                            currentChip.setChipBackgroundColorResource(R.color.chipDefault);
////                            currentChip.setTextColor(getResources().getColor(R.color.black));
////                        }
////                    }
////                    chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
////                    chip.setTextColor(getResources().getColor(android.R.color.white));
////                    categorySelected = (String) chip.getText();
////                    Log.d("chups", categorySelected);
////                }
////            });
//
//            cg_store.addView(chip);
//        }

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        mapView = findViewById(R.id.mapViewNew);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.getController().setCenter(new GeoPoint(14.56610, 120.99244));
        mapView.getController().setZoom(19);
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
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mapView.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mapView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                onTouchEvent(motionEvent);
                return false;
            }
        });

        btn_upload.setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_PICK);
            intent2.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent2);
        });

        btn_register_store.setOnClickListener(v -> {

            name = String.valueOf(name_text_input.getText());
            description = String.valueOf(description_text_input.getText());
            location = String.valueOf(location_text_input.getText());
//            category = category_spinner.getSelectedItem().toString().toLowerCase();
            rating = 0.0F;
            start_time = Integer.parseInt(String.valueOf(start_time_text_input.getText()));
            end_time = Integer.parseInt(String.valueOf(end_time_text_input.getText()));

            ByteArrayOutputStream byteArrayOutputStream;
            byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            StoreModel store = new StoreModel();
            store.setStore_name(name);
            store.setStore_description(description);
            store.setStore_location(location);
//            store.setStore_category(category);
            store.setStore_rating(rating);
            store.setStore_open(start_time);
            store.setStore_closing(end_time);

            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
//            intent2.putExtra("Username", uname);
//            intent2.putExtra("Email", email);
//            intent2.putExtra("Number", number);
//            intent2.putExtra("Password",password);
//            intent2.putExtra("Store",store);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            SignUpStore(name,description,location,rating,start_time,end_time,image, rlat, rlong, "pending");

            startActivity(intent2);
        });
    }

    public void Initialize(){
        name_text_input = findViewById(R.id.name_text_input);
        description_text_input = findViewById(R.id.description_text_input);
        location_text_input = findViewById(R.id.location_text_input);
//        category_spinner = findViewById(R.id.category_spinner);
//        cg_store = findViewById(R.id.cg_store);
        start_time_text_input = findViewById(R.id.start_time_text_input);
        end_time_text_input = findViewById(R.id.end_time_text_input);
        iv_store_img = findViewById(R.id.iv_store_img);
        btn_register_store = findViewById(R.id.btn_register_store);
        btn_upload = findViewById(R.id.btn_upload);
    }

    private void extractMerchantId(String uname){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "getId.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    Log.d("REGISTER: success= ", result);
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d("REGISTER: success= ", "3" );
//                    String success = jsonObject.getString("success");
//
//                    Log.d("REGISTER: success= ", success );
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    int idMerchant = jsonObject.getInt("idMerchant");

                    Log.d("idMerchant", String.valueOf(idMerchant));
                    if (idMerchant != 0){
                        merchantId = idMerchant;
                        Log.d("MerchantID", String.valueOf(merchantId));
                    }
                } catch (JSONException e) {
                    Log.d("ID catch:", String.valueOf(e));
                    Toast.makeText(StoreRegister.this, "Catch ",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoreRegister.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", uname);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void SignUpStore(String name_text_input,  String description_text_input,
                                String location_text_input, float rating_text_input,
                             int start_time_text_input, int end_time_text_input,
                             String iv_store_img, double rlat, double rlong, String status){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "testS.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("1 ", result );
                try {
                    Log.d("REGISTER: success= ", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String success = jsonObject.getString("success");

                    Log.d("REGISTER: success= ", success );
                    if (success.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        StoreRegister.this.startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Your Account is Being Processed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StoreRegister.this, "Email/Contact has been used ",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("REGISTER:", "catch" );
                    Toast.makeText(StoreRegister.this, "Error",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StoreRegister.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idStore", String.valueOf(merchantId));
                params.put("storeName", name_text_input);
                params.put("storeDescription", description_text_input);
                params.put("storeLocation", location_text_input);
//                params.put("storeCategory", category_text_input);
                params.put("storeRating", "0");
                params.put("storeImage", base64Image);
                params.put("storeStartTime", String.valueOf(start_time_text_input));
                params.put("storeEndTime", String.valueOf(end_time_text_input));
                params.put("latitude", String.valueOf(rlat));
                params.put("longitude", String.valueOf(rlong));
                params.put("status", status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public int counterChip() {
        int countChip = 0;

        for (int i=0;i<cg_store.getChildCount();i++){
            if (cg_store.getChildAt(i).isSelected()){
                countChip++;
                Log.d("Check Count Chip", String.valueOf(countChip));
            }
        }

        return countChip;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Context context = this;
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            iv_store_img.setImageBitmap(bitmap);
            Log.d("IMAGE" , String.valueOf(iv_store_img.getDrawable()));
        }
    }
}