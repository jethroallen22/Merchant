package com.example.merchant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.StoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StoreRegister extends AppCompatActivity {
    EditText name_text_input, description_text_input, location_text_input, category_text_input, start_time_text_input, end_time_text_input;
    Button btn_upload, btn_register_store;
    ImageView iv_store_img;

    Spinner category_spinner;

    String image, name, description, location, category;
    Float rating, popularity;
    int start_time;
    int end_time;
    String uname, email, number, password;
    private RequestQueue requestQueue1;
    int merchantId=0;

    private static String JSON_URL;
    private IPModel ipModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        getSupportActionBar().hide();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Initialize();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category_spinner.setAdapter(adapter);

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

        btn_register_store.setOnClickListener(v -> {

            name = String.valueOf(name_text_input.getText());
            description = String.valueOf(description_text_input.getText());
            location = String.valueOf(location_text_input.getText());
            category = category_spinner.getSelectedItem().toString().toLowerCase();
            rating = 0.0F;
            start_time = Integer.parseInt(String.valueOf(start_time_text_input.getText()));
            end_time = Integer.parseInt(String.valueOf(end_time_text_input.getText()));

            StoreModel store = new StoreModel();
            store.setStore_name(name);
            store.setStore_description(description);
            store.setStore_location(location);
            store.setStore_category(category);
            store.setStore_rating(rating);
            store.setStore_open(start_time);
            store.setStore_closing(end_time);

            Intent intent2 = new Intent(getApplicationContext(),Home.class);
            intent2.putExtra("Username", uname);
            intent2.putExtra("Email", email);
            intent2.putExtra("Number", number);
            intent2.putExtra("Password",password);
            intent2.putExtra("Store",store);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            SignUpStore(name,description,location,category,rating,start_time,end_time,image, "pending");

            startActivity(intent2);
        });
    }

    public void Initialize(){
        name_text_input = findViewById(R.id.name_text_input);
        description_text_input = findViewById(R.id.description_text_input);
        location_text_input = findViewById(R.id.location_text_input);
        category_spinner = findViewById(R.id.category_spinner);
        start_time_text_input = findViewById(R.id.start_time_text_input);
        end_time_text_input = findViewById(R.id.end_time_text_input);
        iv_store_img = findViewById(R.id.iv_store_img);
        btn_register_store = findViewById(R.id.btn_register_store);
        btn_upload = findViewById(R.id.btn_upload);
    }

//    public void extractMerchantId(String uname){
////        Log.d("JSON_URL_MERCHANT: ", JSON_URL_MERCHANT);
//
//        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.POST, JSON_URL_MERCHANT + "getId.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("ExtractID: ", "After Response");
//                try {
//                    Log.d("ExtractID: ", "Im in");
//                    JSONObject jsonObjectRec1 = response.getJSONObject(0);
//
//                    int idMerchant = jsonObjectRec1.getInt("idMerchant");
//
//                    Log.d("idMerchant", String.valueOf(idMerchant));
//                    if (idMerchant != 0){
//                        merchantId = idMerchant;
//                        Log.d("MerchantID", String.valueOf(merchantId));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("OnError P: ", String.valueOf(error));
//            }
//        })
//        {
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name", uname);
//                return params;
//            }
//        };
//        requestQueue1.add(jsonArrayRequestRec1);
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        requestQueue.add(stringRequest);
//    }

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
                                String location_text_input, String category_text_input,
                             float rating_text_input,
                             int start_time_text_input, int end_time_text_input,
                             String iv_store_img, String status){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "testS.php", new Response.Listener<String>() {
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
                params.put("idStore", String.valueOf(merchantId));
                params.put("storeName", name_text_input);
                params.put("storeDescription", description_text_input);
                params.put("storeLocation", location_text_input);
                params.put("storeCategory", category_text_input);
                params.put("storeRating", "0");
                params.put("storeImage", "http://www.healitall.com/wp-content/uploads/2018/06/chicken.jpg");
                params.put("storeStartTime", String.valueOf(start_time_text_input));
                params.put("storeEndTime", String.valueOf(end_time_text_input));
                params.put("status", status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}