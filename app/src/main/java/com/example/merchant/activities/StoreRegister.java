package com.example.merchant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.models.StoreModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StoreRegister extends AppCompatActivity {
    EditText name_text_input, description_text_input, location_text_input, category_text_input, start_time_text_input, end_time_text_input;
    Button btn_upload, btn_register_store;
    ImageView iv_store_img;

    String image, name, description, location, category;
    Float rating, popularity;
    String start_time, end_time;
    String uname, email, number, password;
    int merch_id;

    private static String JSON_URL_MERCHANT= "http://10.154.162.184/mosibus_php/merchant/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        getSupportActionBar().hide();
        Initialize();

        Intent intent = getIntent();
        if (intent != null){
            merch_id = intent.getIntExtra("idMerchant", 0);
            uname = intent.getStringExtra("Name");
            email = intent.getStringExtra("Email");
            number = intent.getStringExtra("Number");
            password = intent.getStringExtra("Password");
            Log.d("USER", "UN: " + uname);
        }

        btn_register_store.setOnClickListener(v -> {

            name = String.valueOf(name_text_input.getText());
            description = String.valueOf(description_text_input.getText());
            location = String.valueOf(location_text_input.getText());
            category = String.valueOf(category_text_input.getText());
            rating = 0.0F;
            popularity = 0.0F;
            start_time = String.valueOf(start_time_text_input.getText());
            end_time = String.valueOf(end_time_text_input.getText());

            StoreModel store = new StoreModel();
            store.setStore_name(name);
            store.setStore_description(description);
            store.setStore_location(location);
            store.setStore_category(category);
            store.setStore_rating(rating);
            store.setStore_popularity(popularity);
            store.setStore_open(start_time);
            store.setStore_closing(end_time);

            Intent intent2 = new Intent(getApplicationContext(),Home.class);
            intent2.putExtra("Username", uname);
            intent2.putExtra("Email", email);
            intent2.putExtra("Number", number);
            intent2.putExtra("Password",password);
            intent2.putExtra("Store",store);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            SignUpMerchant(uname, email, number, password);
            SignUpStore(name,description,location,category,rating,popularity,start_time,end_time,image);

            startActivity(intent2);
        });
    }

    public void Initialize(){
        name_text_input = findViewById(R.id.name_text_input);
        description_text_input = findViewById(R.id.description_text_input);
        location_text_input = findViewById(R.id.location_text_input);
        category_text_input = findViewById(R.id.category_text_input);
        start_time_text_input = findViewById(R.id.start_time_text_input);
        end_time_text_input = findViewById(R.id.end_time_text_input);
        iv_store_img = findViewById(R.id.iv_store_img);
        btn_register_store = findViewById(R.id.btn_register_store);
        btn_upload = findViewById(R.id.btn_upload);
    }

    private void SignUpMerchant(String uname,  String email,
                        String number, String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_MERCHANT + "testM.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("1 ", result );
                try {
                    Log.d("REGISTER: success= ", result);
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("REGISTER: success= ", "3" );
                    String success = jsonObject.getString("success");

                    Log.d("REGISTER: success= ", success );
                    if (success.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        StoreRegister.this.startActivity(intent);
                    } else {
                        Toast.makeText(StoreRegister.this, "Email/Contact has been used ",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("REGISTER:", "catch" );
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
                params.put("email", email);
                params.put("contact", number);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void SignUpStore(String name_text_input,  String description_text_input,
                                String location_text_input, String category_text_input,
                             float rating_text_input, float popularity_text_input,
                             String start_time_text_input, String end_time_text_input,
                             String iv_store_img){

//        name_text_input = findViewById(R.id.name_text_input);
//        description_text_input = findViewById(R.id.description_text_input);
//        location_text_input = findViewById(R.id.location_text_input);
//        category_text_input = findViewById(R.id.category_text_input);
//        rating_text_input = findViewById(R.id.rating_text_input);
//        popularity_text_input = findViewById(R.id.popularity_text_input);
//        start_time_text_input = findViewById(R.id.start_time_text_input);
//        end_time_text_input = findViewById(R.id.end_time_text_input);
//        iv_store_img = findViewById(R.id.iv_store_img);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_MERCHANT + "testS.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("1 ", result );
                try {
                    Log.d("REGISTER: success= ", result);
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("REGISTER: success= ", "3" );
                    String success = jsonObject.getString("success");

                    Log.d("REGISTER: success= ", success );
                    if (success.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        StoreRegister.this.startActivity(intent);
                    } else {
                        Toast.makeText(StoreRegister.this, "Email/Contact has been used ",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.d("REGISTER:", e.toString() );
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
                params.put("idStore", String.valueOf(merch_id + 1));
                params.put("storeName", name_text_input);
                params.put("storeDescription", description_text_input);
                params.put("storeLocation", location_text_input);
                params.put("storeCategory", category_text_input);
                params.put("storeRating", "0");
                params.put("storePopularity", "0");
                params.put("storeImage", "http://www.healitall.com/wp-content/uploads/2018/06/chicken.jpg");
                params.put("storeStartTime", start_time_text_input);
                params.put("storeEndTime", end_time_text_input);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}