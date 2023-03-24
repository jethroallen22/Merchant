package com.example.merchant.activities.ui.ordersummary;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.ui.orders.OrdersFragment;
import com.example.merchant.activities.ui.slideshow.ProductsViewModel;
import com.example.merchant.adapters.OrderItemsAdapter;
import com.example.merchant.databinding.FragmentOrderSummaryBinding;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummaryFragment extends Fragment {

    private FragmentOrderSummaryBinding binding;
    //Cart List Recycler View
    RecyclerView rv_order_items;
    OrderModel order;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;
    TextView tv_order_id, tv_name, tv_total_price;
    Button btn_complete_order, btn_cancel_order, btn_confirm_order, btn_pickup_order;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_order_items = root.findViewById(R.id.rv_order_items);
        tv_order_id = root.findViewById(R.id.tv_order_id);
        tv_name = root.findViewById(R.id.tv_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_confirm_order = root.findViewById(R.id.btn_confirm_order);
        btn_cancel_order = root.findViewById(R.id.btn_cancel_order);
        btn_complete_order = root.findViewById(R.id.btn_complete_order);
        btn_pickup_order = root.findViewById(R.id.btn_pickup_order);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            order = new OrderModel();
            order = bundle.getParcelable("Order");
        }
        tv_order_id.setText(String.valueOf(order.getIdOrder()));
        tv_name.setText(String.valueOf(order.getUsers_name()));
        tv_total_price.setText(String.valueOf(order.getOrderItemTotalPrice()));
        //tv_address.setText(order.getAddress());
        //tv_distance.setText("Distance from you: " + order.getDistance() + "km");

        order_item_list = new ArrayList<>();
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));

        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list());
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        if (order.getOrderStatus().equals("pending")){
            Log.d("INSIDE IF", "it is preparing already");
            btn_confirm_order.setEnabled(true);
            btn_pickup_order.setEnabled(false);
            btn_complete_order.setEnabled(false);
        }

        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BTN IdOrder: ", String.valueOf(order.getIdOrder()));
                UpdateStatus(order.getIdOrder(), "preparing");
                Log.d("TAG", "Success");
                btn_confirm_order.setEnabled(false);
                btn_pickup_order.setEnabled(true);
                btn_complete_order.setEnabled(false);
                btn_cancel_order.setEnabled(false);
            }
        });

        btn_pickup_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus(order.getIdOrder(), "pickup");
                btn_confirm_order.setEnabled(false);
                btn_pickup_order.setEnabled(false);
                btn_complete_order.setEnabled(true);
                btn_cancel_order.setEnabled(false);
            }
        });

        btn_complete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BTN IdOrder: ", String.valueOf(order.getIdOrder()));
                UpdateStatus(order.getIdOrder(), "completed");
                OrdersFragment fragment = new OrdersFragment();
                Log.d("TAG", "Success");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BTN IdOrder: ", String.valueOf(order.getIdOrder()));
                UpdateStatus(order.getIdOrder(), "canceled");
                OrdersFragment fragment = new OrdersFragment();
                Log.d("TAG", "Success");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void UpdateStatus(int idOrder, String status){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "testO.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", String.valueOf(error));
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idOrder", String.valueOf(idOrder));
                params.put("orderStatus", status);
//                Log.d("Params: ", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
}