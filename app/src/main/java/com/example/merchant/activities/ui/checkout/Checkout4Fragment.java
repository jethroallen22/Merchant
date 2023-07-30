package com.example.merchant.activities.ui.checkout;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.Home;
import com.example.merchant.activities.ui.payout.PayoutFragment;
import com.example.merchant.activities.ui.reports.ReportsFragment;
import com.example.merchant.databinding.FragmentCheckout4Binding;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout4Fragment extends Fragment {

    private FragmentCheckout4Binding binding;
    OrderModel orderModel;
    private static String JSON_URL;
    private IPModel ipModel;
    RequestQueue requestQueueOrder;
    List<OrderModel> orderModelList;
    private Home mActivity;

    NotificationManager manager;

    TextView total, id3, amount;

    Button btn_next;
    float expense;
    String dateTimeString;
    int id;
    public static String name = "";
    public static String email = "";

    @SuppressLint("MissingPermission")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout4Binding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        total = root.findViewById(R.id.tv_total_amount);
        amount = root.findViewById(R.id.tv_amount);
        btn_next = root.findViewById(R.id.btn_next4);

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            expense = bundle.getFloat("expense");
            dateTimeString = bundle.getString("datetime");
        }
        orderModelList = new ArrayList<>();
        requestQueueOrder = Singleton.getsInstance(getActivity()).getRequestQueue();

//        Log.d("checkout", String.valueOf(orderModel.getOrderItemTotalPrice()));
        total.setText(String.valueOf(expense));
//        Log.d("checkout", total.getText().toString());
        amount.setText(String.valueOf(expense));


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Payout();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(), "My Notification");
                builder.setContentTitle("Mosibus");
                builder.setContentText("You have Successfully Sent Your Payout to the Admin");
                builder.setSmallIcon(R.drawable.logo);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
                managerCompat.notify(1, builder.build());

                NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
                manager = (NotificationManager) getSystemService(getActivity().getApplicationContext(), NotificationManager.class);
                manager.createNotificationChannel(channel);
                Bundle bundle = new Bundle();
                PayoutFragment fragment = new PayoutFragment();
                bundle.putString("name", name);
                bundle.putInt("idMerchant", id);
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Home) getActivity();
    }

    public void Payout(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "payout.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

//                        Toast.makeText(getActivity(), "Your Product has been Removed", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("merchantId", String.valueOf(id));
                paramV.put("transacType", "payout");
                paramV.put("amount", String.valueOf(expense));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}