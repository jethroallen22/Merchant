package com.example.merchant.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.ui.addproduct.AddProductFragment;
import com.example.merchant.activities.ui.deals.DealsStatusFragment;
import com.example.merchant.activities.ui.profile.ProfileFragment;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.activities.ui.special.SpecialFragment;
import com.example.merchant.activities.ui.special.SpecialStatusFragment;
import com.example.merchant.activities.ui.voucher.VoucherStatusFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.databinding.ActivityHomeBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.MerchantModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.StoreModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.merchant.activities.databinding.ActivityHomeBinding;
import com.example.merchant.databinding.ActivityHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private TextView tv_view_profile, tv_incoming_orders, tv_new_customers;
//    private TableLayout tl_dashboard;
    public static String name = "";
    public static String email = "";
    public static int id;
    OrderAdapter orderAdapter;
    List <StoreModel> storeList;
    private RequestQueue requestQueue1,requestQueue3, requestQueue;
    List<OrderItemModel> order_item_list;
    List<OrderItemModel> temp_order_item;
    List<OrderModel> order_list;
    List<OrderModel> temp_order;
    int temp_idOrder = 0;
    int incoming_count = 0;
    int newcust_count = 0;

    Handler root;

    ImageView iv_user_image;
    TextView tv_user_name;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    List<StoreModel> storeModelList;
    StoreModel storeModel;
    String image, endDate, formattedDateTime;
    Bitmap bitmap;
    Date currDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        currDate = new Date();
        Log.d("HOME", "Inside Home");
        Log.d("HOME DATE", String.valueOf(currDate));

        Intent intent = getIntent();
        if(intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            id = intent.getIntExtra("idMerchant",0);
            email = intent.getStringExtra("email");
            Log.d("HOME FRAG name", name + id + email);
        } else {
            Log.d("HOME FRAG name", "FAIL");
        }

        requestQueue1 = Singleton.getsInstance(this).getRequestQueue();
        requestQueue = Singleton.getsInstance(this).getRequestQueue();

        // Format the date and time.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDateTime = dateFormat.format(currDate);
//        getEndDate();


        root = new Handler();
        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                store_profile();
                root.postDelayed(this, 10000);
            }
        }, 0);


        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_orders, R.id.nav_products, R.id.nav_deals, R.id.nav_voucher, R.id.nav_special)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        iv_user_image = navigationView.getHeaderView(0).findViewById(R.id.iv_user_image);
        tv_user_name = navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        tv_view_profile = navigationView.getHeaderView(0).findViewById(R.id.tv_view_profile);

//        byte[] byteArray = Base64.decode(image, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
//        iv_user_image.setImageBitmap(bitmap);
//        Log.d("IMAGE", String.valueOf(bitmap));
//        tv_user_name.setText(name);
        tv_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle = new Bundle()
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                Log.d("view prof click", storeModel.getStore_name());
                bundle.putParcelable("stores", storeModel);

                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                Log.d("CLICK", "CLICK");
            }
        });

        navigationView.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                Bundle bundle = new Bundle();
                ProductsFragment fragment = new ProductsFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                return false;
            }
        });
        navigationView.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                Bundle bundle = new Bundle();
                DealsStatusFragment fragment = new DealsStatusFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                return false;
            }
        });
        navigationView.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                Bundle bundle = new Bundle();
                VoucherStatusFragment fragment = new VoucherStatusFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                return false;
            }
        });
        navigationView.getMenu().getItem(5).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                Bundle bundle = new Bundle();
                SpecialStatusFragment fragment = new SpecialStatusFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                return false;
            }
        });

    }

    public void store_profile(){
        Log.d("Store Profile", "called");
        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "api.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Store Profile Resp: ", String.valueOf(response.length()));
                Log.d("Store Profile id", String.valueOf(id));
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
                        Log.d("Store Profile idStore", String.valueOf(jsonObjectRec1.getInt("idStore")));
                        if (id == jsonObjectRec1.getInt("idStore")){
                            Log.d("Store Profile", "inside if");
                            Log.d("Store Profile", jsonObjectRec1.getString("storeName"));
                            name = jsonObjectRec1.getString("storeName");
                            image = jsonObjectRec1.getString("storeImage");
                            byte[] byteArray = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
                            iv_user_image.setImageBitmap(bitmap2);
                            Log.d("IMAGE", String.valueOf(bitmap));
                            tv_user_name.setText(name);

                            Log.d("Store Profile", "after set");
                            long r_id = jsonObjectRec1.getLong("idStore");
                            String r_image = jsonObjectRec1.getString("storeImage");
                            String r_name = jsonObjectRec1.getString("storeName");
                            String r_description = jsonObjectRec1.getString("storeDescription");
                            String r_location = jsonObjectRec1.getString("storeLocation");
                            String r_category = jsonObjectRec1.getString("storeCategory");
                            float r_rating = (float) jsonObjectRec1.getDouble("storeRating");
                            int r_open = jsonObjectRec1.getInt("storeStartTime");
                            int r_close = jsonObjectRec1.getInt("storeEndTime");

                            storeModel = new StoreModel(r_id,r_image,r_name,r_description,r_location,r_category,
                                    r_rating, r_open, r_close);
                            Log.d("Store Profile", storeModel.getStore_name());
//                            storeModelList.add(storeModel);
//                            Log.d("Store Profile", "list added");
//                            Log.d("Store Profile", String.valueOf(storeModelList.size()));
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if(storeModel.getBitmapImage() != null) {
//                    iv_user_image.setImageBitmap(storeModel.getBitmapImage());
//                } else
//                    iv_user_image.setImageResource(R.drawable.logo);
//                tv_user_name.setText(storeModel.getStore_name());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnError P: ", String.valueOf(error));
            }
        });
        requestQueue1.add(jsonArrayRequestRec1);
    }

    public void getEndDate(){
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"specialNotif.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectNotif = response.getJSONObject(i);
                        endDate = jsonObjectNotif.getString("endDate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (endDate == null){
                    Log.d("home date check", "is null");
                    NavigationView navigationView = binding.navView;
                    navigationView.getMenu().getItem(5).setVisible(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}