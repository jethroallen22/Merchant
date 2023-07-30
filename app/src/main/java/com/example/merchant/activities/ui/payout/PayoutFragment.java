package com.example.merchant.activities.ui.payout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.merchant.R;
import com.example.merchant.activities.ui.checkout.CheckoutFragment;
import com.example.merchant.activities.ui.feedback.FeedbackSummaryFragment;
import com.example.merchant.adapters.PayoutAdapter;
import com.example.merchant.databinding.FragmentFeedbackBinding;
import com.example.merchant.databinding.FragmentPayoutBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.PayoutModel;
import com.example.merchant.models.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PayoutFragment extends Fragment implements RecyclerViewInterface {
    private FragmentPayoutBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;
    RecyclerView rv_payout;
    TextView tv_balance;
    Button btn_payout;
    List<PayoutModel> payoutModelList;
    PayoutAdapter payoutAdapter;
    List<ProductModel> product_list;
    private RequestQueue requestQueue1, requestQueue4, requestQueue5;

    public static String name = "";
    public static String email = "";
    public static int id;

    float orderItemTotalPrice;
    String dateTimeString, timedate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPayoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if(intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            id = intent.getIntExtra("idMerchant",0);
            email = intent.getStringExtra("email");
            Log.d("Orders name", name + id + email);
        } else {
            Log.d("Orders name", "FAIL");
        }

        rv_payout = root.findViewById(R.id.rv_payout);
        tv_balance = root.findViewById(R.id.tv_balance);
        btn_payout = root.findViewById(R.id.btn_payout);
        requestQueue1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue4 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue5 = Singleton.getsInstance(getActivity()).getRequestQueue();
        Log.d("Start ", "Before ExtractOrderItem");
        product_list = new ArrayList<>();
        payoutModelList = new ArrayList<>();

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDateAndTime = calendar.getTime();
        // Format the date and time as desired
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDateAndTime);
        String formattedTime = timeFormat.format(currentDateAndTime);

        dateTimeString = formattedDate + " " + formattedTime;

        getPayoutHistory();
        sales(String.valueOf(id));

        btn_payout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                CheckoutFragment fragment = new CheckoutFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                bundle.putFloat("expense", orderItemTotalPrice);
                bundle.putString("datetime", dateTimeString);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });

        return root;
    }

    public void getPayoutHistory(){

        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"payout.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject2 = response.getJSONObject(i);
//                        int id = jsonObject2.getInt("userId");
                        if(id == jsonObject2.getInt("merchantId")) {
                            String transacType = jsonObject2.getString("transacType");
                            String date = jsonObject2.getString("date");
                            double amount = jsonObject2.getDouble("amount");
                            PayoutModel payoutModel = new PayoutModel(transacType, date, amount);
                            payoutModelList.add(payoutModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                payoutAdapter = new PayoutAdapter(getActivity(),payoutModelList);
                rv_payout.setAdapter(payoutAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_payout.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue4.add(jsonArrayRequestFoodforyou);
    }

    private void sales(String storeId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "sales.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
//                    Log.d("NewCust: success= ", result);
                    JSONArray jsonArray = new JSONArray(result);
//                    Log.d("NewCust: Array Length ", String.valueOf(jsonArray.length()));
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        orderItemTotalPrice = jsonObject.getInt("orderItemTotalPrice");
                        orderItemTotalPrice = (float)Math.round((orderItemTotalPrice * 0.05) * 100) / 100;

                        tv_balance.setText("Balance: ₱ "+String.valueOf(orderItemTotalPrice));
                    }
//                    Log.d("NewCust:", "Outside Loop");
//                    tv_new_customers.setText(String.valueOf(newcust_count));
                } catch (JSONException e) {
                    Log.d("Catch:", String.valueOf(e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("storeId", storeId);
                return params;
            }
        };

//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue1.add(stringRequest);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onItemClick(int position) {
//        Log.d("Test", "Click");
//        Bundle order = new Bundle();
//        FeedbackSummaryFragment fragment = new FeedbackSummaryFragment();
//        order.putParcelable("Order", payoutModelList.get(position));
//        fragment.setArguments(order);
//        Log.d("TAG", "Success");
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
//        Log.d("TAG", "Success Click");
    }

    @Override
    public void onItemClickEdit(int position) {

    }
}