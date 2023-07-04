package com.example.merchant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText register_name_text_input, register_email_text_input,
            register_number_text_input, register_password_text_input,
            register_confpassword_text_input;
    private Button signup_btn;
    private TextView tv_login_btn;

    //Workspace IP
    private static String JSON_URL;
    private IPModel ipModel;

    private MapView mMapView;
    private MapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        init();

        register_name_text_input = findViewById(R.id.name_text_input);
        register_email_text_input = findViewById(R.id.description_text_input);
        register_number_text_input = findViewById(R.id.contact_text_input);
        register_password_text_input = findViewById(R.id.register_password_text_input);
        register_confpassword_text_input = findViewById(R.id.confpassword_text_input);

        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();

        File tileCache = new File(getCacheDir().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

//        mMapView = findViewById(R.id.mapView);
//        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
//        mMapView.setBuiltInZoomControls(true);
//        mMapController = (MapController) mMapView.getController();
//        mMapController.setZoom(13);
//        GeoPoint gPt = new GeoPoint(29.624471, 52.523935);
//        mMapController.setCenter(gPt);
        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Register.this.startActivity(intent);
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rname = register_name_text_input.getText().toString().trim();
                String remail = register_email_text_input.getText().toString().trim();
                String rnumber = register_number_text_input.getText().toString().trim();
                String rpassword = register_password_text_input.getText().toString().trim();
                String rconfpassword = register_confpassword_text_input.getText().toString().trim();

//                if (!rpassword.equals(rconfpassword)){
//                    register_password_text_input.setError("Passwords do not match");
//                    register_confpassword_text_input.setError("Passwords do not match");
//                } else if (!rname.equals("")&&!remail.equals("")&&!rnumber.equals("")&&!rpassword.equals("")){
//                    SignUp(rname, remail, rnumber, rpassword);
//
//                }
                SignUpMerchant(rname, remail, rnumber, rpassword);
            }
        });

    }

    public void init(){
        signup_btn = (Button) findViewById(R.id.btn_add_product);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_btn);
    }

    private void SignUpMerchant(String uname,  String email,
                                String number, String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "testM.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("1 ", result );
                try {
                    Log.d("REGISTER: success= ", result);
//                    JSONObject jsonObject = new JSONObject(result);
                    Log.d("REGISTER: success= ", "3" );
//                    String success = jsonObject.getString("success");

//                    Log.d("REGISTER: success= ", success );
//                    if (success.equals("1")){
                        Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                        intent.putExtra("Name", uname);
                        intent.putExtra("Email", email);
                        intent.putExtra("Number", number);
                        intent.putExtra("Password", password);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        Register.this.startActivity(intent);
//                    } else {
//                        Toast.makeText(Register.this, "Email/Contact has been used ",Toast.LENGTH_SHORT).show();
//                    }
                } catch (Error e) {
                    Log.d("REGISTER:", "catch" );
                    Toast.makeText(Register.this, "Catch ",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", uname);
                params.put("email", email);
                params.put("contact", number);
                params.put("password", password);
                params.put("status", "pending");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void Check(String register_email_text_input, String register_number_text_input, String inputEmail, String inputNumber){

    }
}