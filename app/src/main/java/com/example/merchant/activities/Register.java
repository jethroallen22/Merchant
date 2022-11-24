package com.example.merchant.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText register_name_text_input, register_email_text_input,
            register_number_text_input, register_password_text_input,
            register_confpassword_text_input;
    private Button signup_btn;
    private TextView tv_login_btn;

    //Workspace IP
    private static String URL_SIGNUP = "http://192.168.68.106/android_register_login/register.php";
    private static String URL_SIGNUP1 = "http://192.168.68.106/android_register_login/resgister1.php";
    private static String URL_CHECK = "http://192.168.68.109/android_register_login/apiusers.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        init();

        register_name_text_input = findViewById(R.id.name_text_input);
        register_email_text_input = findViewById(R.id.description_text_input);
        register_number_text_input = findViewById(R.id.contact_text_input);
        register_password_text_input = findViewById(R.id.calories_text_input);
        register_confpassword_text_input = findViewById(R.id.price_text_input);

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
                Intent intent = new Intent(getApplicationContext(), StoreRegister.class);
                intent.putExtra("Name", rname);
                intent.putExtra("Email", remail);
                intent.putExtra("Number", rnumber);
                intent.putExtra("Password", rpassword);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Register.this.startActivity(intent);
            }
        });

    }

    public void init(){
        signup_btn = (Button) findViewById(R.id.btn_add_product);
        tv_login_btn = (TextView) findViewById(R.id.tv_login_btn);
    }

    private void SignUp(String register_name_text_input,  String register_email_text_input,
                        String register_number_text_input, String register_password_text_input){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP1, new Response.Listener<String>() {
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
                        Register.this.startActivity(intent);
                    } else {
                        Toast.makeText(Register.this, "Email/Contact has been used ",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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
                params.put("name", register_name_text_input);
                params.put("email", register_email_text_input);
                params.put("contact", register_number_text_input);
                params.put("password", register_password_text_input);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void Check(String register_email_text_input, String register_number_text_input, String inputEmail, String inputNumber){

    }
}