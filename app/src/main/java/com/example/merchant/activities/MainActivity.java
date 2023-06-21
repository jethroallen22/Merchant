package com.example.merchant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.models.IPModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText login_email_text_input, login_password_text_input;
    private Button login_btn;
    private TextView tv_register_btn;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();
        init();

        tv_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Register.class);
                Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input checker
                String mEmail = login_email_text_input.getText().toString().trim();
                String mPass = login_password_text_input.getText().toString().trim();

                if (mEmail.isEmpty() || mPass.isEmpty()){
                    if (mEmail.isEmpty())
                        login_email_text_input.setError("Please insert Email!");
                    if (mPass.isEmpty())
                        login_password_text_input.setError("Please insert Password!");
                } else {
                    LogIn(mEmail, mPass);
//                LogIn("nathan83@yahoo.com", "test");
                }

//                Intent intent = new Intent(getApplicationContext(), Home.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                MainActivity.this.startActivity(intent);
            }
        });
    }

    public void init(){
        login_email_text_input = findViewById(R.id.login_email_text_input);
        login_password_text_input = findViewById(R.id.login_password_text_input);

        login_btn = findViewById(R.id.login_btn);
        tv_register_btn = findViewById(R.id.tv_register_btn);
    }

    //Login
    private void LogIn(String login_email_text_input, String login_password_text_input){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL+"loginM.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    Log.d("responseJson", String.valueOf(jsonObject));


                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    int id = 0;
                    String name = "";
                    String email = "";

                    Log.d("OnRespo: ", success);
                    if (success.equals("1")){
                        Log.d("INSIDE IF:", "SUCCESS");
                        for (int i = 0; i < jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            name = object.getString("name").trim();
                            email = object.getString("email").trim();
                            id = object.getInt("idMerchant");

                            Toast.makeText(MainActivity.this, "Success Login. \nYour Name : "
                                    + name + "\nYour Email : "
                                    + email + " ID: " + id, Toast.LENGTH_SHORT).show();

                            Log.d("HELLO", name + email);
                        }
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("name",name);
                        intent.putExtra("idMerchant",id);
                        intent.putExtra("email",email);
                        Log.d("NAME LOGIN: " , String.valueOf(id));
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                } catch (JSONException e) {

//                    e.printStackTrace();
//                    Toast.makeText(Login.this, "Error! "+ e.toString(),Toast.LENGTH_SHORT).show();*/

                    Toast.makeText(MainActivity.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", login_email_text_input);
                params.put("password", login_password_text_input);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}