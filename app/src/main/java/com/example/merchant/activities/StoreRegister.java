package com.example.merchant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.merchant.R;
import com.example.merchant.models.StoreModel;

public class StoreRegister extends AppCompatActivity {
    EditText name_text_input, description_text_input, location_text_input, category_text_input, rating_text_input, popularity_text_input, start_time_text_input, end_time_text_input;
    Button btn_upload, btn_register_store;
    ImageView iv_store_img;

    String image, name, description, locatiom, category;
    Float rating, popularity;
    String start_time, end_time;
    String uname, email, number, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        getSupportActionBar().hide();
        Initialize();

        Intent intent = getIntent();
        if (intent != null){
            uname = intent.getStringExtra("Name");
            email = intent.getStringExtra("Email");
            number = intent.getStringExtra("Number");
            password = intent.getStringExtra("Password");
            Log.d("USER", "UN: " + uname);
        }

        btn_register_store.setOnClickListener(v -> {

            name = String.valueOf(name_text_input.getText());
            description = String.valueOf(description_text_input.getText());
            locatiom = String.valueOf(location_text_input.getText());
            category = String.valueOf(category_text_input.getText());
            rating = Float.parseFloat(String.valueOf(rating_text_input.getText()));
            popularity = Float.parseFloat(String.valueOf(popularity_text_input.getText()));
            start_time = String.valueOf(start_time_text_input.getText());
            end_time = String.valueOf(end_time_text_input.getText());

            StoreModel store = new StoreModel();
            store.setStoreName(name);
            store.setStoreDescription(description);
            store.setStoreLocation(locatiom);
            store.setStoreCategory(category);
            store.setStoreRating(rating);
            store.setStorePopularity(popularity);
            store.setStoreStartTime(start_time);
            store.setStoreEndTime(end_time);

            Intent intent2 = new Intent(getApplicationContext(),Home.class);
            intent2.putExtra("Username", uname);
            intent2.putExtra("Email", email);
            intent2.putExtra("Number", number);
            intent2.putExtra("Password",password);
            intent2.putExtra("Store",store);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
        });
    }

    public void Initialize(){
        name_text_input = findViewById(R.id.name_text_input);
        description_text_input = findViewById(R.id.description_text_input);
        location_text_input = findViewById(R.id.location_text_input);
        category_text_input = findViewById(R.id.category_text_input);
        rating_text_input = findViewById(R.id.rating_text_input);
        popularity_text_input = findViewById(R.id.popularity_text_input);
        start_time_text_input = findViewById(R.id.start_time_text_input);
        end_time_text_input = findViewById(R.id.end_time_text_input);
        iv_store_img = findViewById(R.id.iv_store_img);
        btn_register_store = findViewById(R.id.btn_register_store);
        btn_upload = findViewById(R.id.btn_upload);
    }
}