package com.example.merchant.activities.ui.orders;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.ui.ordersummary.OrderSummaryFragment;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.adapters.OrderItemsAdapter;
import com.example.merchant.adapters.ProductAdapter;
import com.example.merchant.databinding.FragmentOrdersBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrdersFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOrdersBinding binding;
    //Cart List Recycler View
    RecyclerView rv_orders;
    List<OrderModel> orderModelList;
    OrderAdapter orderAdapter;
    List<ProductModel> product_list;
    private RequestQueue requestQueue4, requestQueue5;
    private static String JSON_URL;
    private IPModel ipModel;

    public static String name = "";
    public static String email = "";
    public static int id;
    String dateTimeString, timedate;
    String endTimeMilitary, endTimeStandard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrdersViewModel ordersViewModel =
                new ViewModelProvider(this).get(OrdersViewModel.class);

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
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

        rv_orders = root.findViewById(R.id.rv_orders);
        requestQueue4 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue5 = Singleton.getsInstance(getActivity()).getRequestQueue();
        Log.d("Start ", "Before ExtractOrderItem");
        product_list = new ArrayList<>();
        orderModelList = new ArrayList<>();

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDateAndTime = calendar.getTime();
        // Format the date and time as desired
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDateAndTime);
        String formattedTime = timeFormat.format(currentDateAndTime);

        dateTimeString = formattedDate + " " + formattedTime;

//        //Convert endTime
//        SimpleDateFormat militaryTimeFormat = new SimpleDateFormat("HHmm");
//        SimpleDateFormat standardTimeFormat = new SimpleDateFormat("hh:mm a");
//
//        Date date = null;
//        try {
//            date = militaryTimeFormat.parse(militaryTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        standardTimeFormat.format(date);

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                extractOrder();
                endOfDay();
                //Log.d("OrderStatus", order.getOrderStatus());
                root.postDelayed(this, 10000);
            }
        }, 0);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {

        Log.d("Test", "Click");
        Bundle order = new Bundle();
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        order.putParcelable("Order",orderModelList.get(position));
        fragment.setArguments(order);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("TAG", "Success Click");
    }

    @Override
    public void onItemClickEdit(int position) {

    }

    public void extractOrder(){
        final List<OrderModel> order_list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequestOrder = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseLength", String.valueOf(response));
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObjectOrder = response.getJSONObject(i);
                        int users_id = jsonObjectOrder.getInt("users_id");
                        int idOrder = jsonObjectOrder.getInt("idOrder");
                        float orderItemTotalPrice = jsonObjectOrder.getInt("orderItemTotalPrice");
                        String orderStatus = jsonObjectOrder.getString("orderStatus");
                        int store_idStore = jsonObjectOrder.getInt("store_idStore");
                        String name = jsonObjectOrder.getString("name");
                         timedate = jsonObjectOrder.getString("timedate");
                        Log.d("ResponseIdStore", String.valueOf(store_idStore));
                        Log.d("ResponseIdStoreMerch", String.valueOf(id));
                        if (store_idStore == id) {
                            Log.d("ResponseMatchIdStore", "Match");
                            OrderModel tempOrderModel = new OrderModel();
                            List<OrderItemModel> tempOrderItemList = new ArrayList<>();
                            tempOrderModel.setIdOrder(idOrder);
                            tempOrderModel.setOrderItemTotalPrice(orderItemTotalPrice);
                            tempOrderModel.setOrderStatus(orderStatus);
                            tempOrderModel.setStore_idstore(store_idStore);
                            tempOrderModel.setUsers_name(name);
                            tempOrderModel.setOrderItem_list(tempOrderItemList);
                            tempOrderModel.setIdUser(users_id);

//                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy hh:mm a" )
                            if (dateTimeString.compareTo(timedate) >= 0){
                                Log.d("DATETIME", "before" + dateTimeString);
                                Log.d("DATETIME DB", timedate);
                                tempOrderModel.setTimedate(null);
                            } else {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                                Date date;
                                try {
                                    date = formatter.parse(timedate);
                                    String formattedDate = formatter2.format(date);
                                    Log.d("DATETIME DB Form", String.valueOf(formattedDate));
                                    tempOrderModel.setTimedate(formattedDate);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            JsonArrayRequest jsonArrayRequestOrderItemList = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiorderitemget.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d("ResponseItemLength", String.valueOf(response.length()));
                                    List<OrderItemModel> orderItemModels = new ArrayList<>();
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonObjectOrderItemList = response.getJSONObject(i);
                                            if (jsonObjectOrderItemList.getInt("idOrder") == idOrder) {
                                                Log.d("ResponseIdOrder", String.valueOf(jsonObjectOrderItemList.getInt("idOrder")));
                                                Log.d("ResponseIdOrderMerch", String.valueOf(idOrder));
                                                Log.d("ResponseIdOrderMatch", "IdOrder Match");
                                                int idProduct = jsonObjectOrderItemList.getInt("idProduct");
                                                int idStore = jsonObjectOrderItemList.getInt("idStore");
                                                int idUser = jsonObjectOrderItemList.getInt("idUser");
                                                int idOrder = jsonObjectOrderItemList.getInt("idOrder");
                                                String productName = jsonObjectOrderItemList.getString("productName");
                                                int itemPrice = jsonObjectOrderItemList.getInt("itemPrice");
                                                int itemQuantity = jsonObjectOrderItemList.getInt("itemQuantity");
                                                float totalPrice = jsonObjectOrderItemList.getInt("totalPrice");
                                                OrderItemModel orderItemModel;
                                                orderItemModel = new OrderItemModel(idProduct, idStore, idUser, idOrder, productName, itemPrice, itemQuantity, totalPrice);

                                                if(orderItemModels.isEmpty())
                                                    orderItemModels.add(orderItemModel);
                                                else {
                                                    for (int k = 0; k < orderItemModels.size(); k++) {
                                                        if (productName.toLowerCase().trim().compareTo(orderItemModels.get(k).getProductName().toLowerCase().trim()) == 0) {
//                                                temp_count = c_productQuantity;
                                                            int tempItemQuantity = 0;
                                                            tempItemQuantity = orderItemModels.get(k).getItemQuantity();
                                                            tempItemQuantity += itemQuantity;
                                                            orderItemModels.get(k).setItemQuantity(tempItemQuantity);
                                                            tempItemQuantity = 0;
                                                            break;
                                                        } else {
                                                            orderItemModels.add(orderItemModel);
                                                            break;
                                                        }
                                                    }
                                                }
                                                //orderItemModels.add(orderItemModel);
                                            }
                                        }
                                        Log.d("ResponseOrderItemSize", String.valueOf(orderItemModels.size()));
                                        tempOrderModel.setOrderItem_list(orderItemModels);
                                        orderModelList = order_list;
                                        Log.d("ResponseOrderSize", String.valueOf(orderModelList.size()));
                                        orderAdapter = new OrderAdapter(getActivity(),orderModelList, OrdersFragment.this);
                                        rv_orders.setAdapter(orderAdapter);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                        rv_orders.setLayoutManager(layoutManager);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("Error", String.valueOf(e));
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error", String.valueOf(error));
                                }
                            });
                            requestQueue5.add(jsonArrayRequestOrderItemList);
                            order_list.add(tempOrderModel);
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue4.add(jsonArrayRequestOrder);

    }

    public void endOfDay(){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "endTime.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("endTime", "inside on res");
                        Log.d("endTime", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject jsonObjectOrderItemList = response.getJSONObject(i);

                            int storeEndTime = jsonObject.getInt("storeEndTime");

                            Log.d("endTime", String.valueOf(storeEndTime));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error", String.valueOf(e));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idStore", String.valueOf(id));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}