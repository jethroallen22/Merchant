package com.example.merchant.activities.ui.Dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.ui.ordersummary.OrderSummaryFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.databinding.ActivityHomeBinding;
import com.example.merchant.databinding.FragmentDashboardBinding;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.StoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView tv_view_profile, tv_incoming_orders, tv_new_customers;
    private TableLayout tl_dashboard;
    public static String name = "";
    public static String email = "";
    public static int id;
    OrderAdapter orderAdapter;
    List <StoreModel> storeList;
    private RequestQueue requestQueue1,requestQueue3;
    List<OrderItemModel> order_item_list;
    List<OrderItemModel> temp_order_item;
    List<OrderModel> order_list;
    List<OrderModel> temp_order;
    int temp_idOrder = 0;

    ImageView iv_user_image;
    TextView tv_user_name;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    List<StoreModel> storeModelList;
    StoreModel storeModel;
    String image;
    Bitmap bitmap;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Intent intent = getActivity().getIntent();
        if(intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            id = intent.getIntExtra("idMerchant",0);
            email = intent.getStringExtra("email");
            Log.d("Dashboard name", name + id + email);
        } else {
            Log.d("Dashboard name", "FAIL");
        }

        //Initialize
        tv_incoming_orders = root.findViewById(R.id.tv_incoming_orders);
        tv_new_customers = root.findViewById(R.id.tv_new_customers);
        tl_dashboard  = root.findViewById(R.id.tl_dashboard);

        storeModelList = new ArrayList();
        order_item_list = new ArrayList<>();
        order_list = new ArrayList<>();
        temp_order_item = new ArrayList<>();
        temp_order = new ArrayList<>();
        requestQueue1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue3 = Singleton.getsInstance(getActivity()).getRequestQueue();

        newCust(String.valueOf(id));
        extractOrders();

        final TextView textView = binding.textHome;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void extractOrders(){
        Log.d("extractOrders", "Called");
        JsonArrayRequest jsonArrayRequestRec3 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "testAll.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Bug", String.valueOf(response));
                Log.d("Response Order: ", String.valueOf(response.length()));
                int incoming_count = 0;
                for (int i=0; i < response.length(); i++){
                    try {
                        Log.d("Try O: ", "Im in");
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);

                        //USERS DB
                        String name = jsonObjectRec1.getString("name");

                        //ORDER DB
                        int idOrder = jsonObjectRec1.getInt("idOrder");
                        int orderItemTotalPrice = jsonObjectRec1.getInt("orderItemTotalPrice");
                        String orderStatus = jsonObjectRec1.getString("orderStatus");
                        int store_idstore = jsonObjectRec1.getInt("store_idstore");

                        //OrderItem DB
                        int idItem = jsonObjectRec1.getInt("idItem");
                        Log.d("Test", String.valueOf(idItem));
                        int product_idProduct = jsonObjectRec1.getInt("product_idProduct");
                        int order_idOrder = jsonObjectRec1.getInt("order_idOrder");
                        float ItemPrice = (float) jsonObjectRec1.getDouble("ItemPrice");
                        int ItemQuantity = jsonObjectRec1.getInt("ItemQuantity");

                        //PRODUCT DB
                        String productName = jsonObjectRec1.getString("productName");
                        Log.d("Store ID", String.valueOf(store_idstore));
                        Log.d("Merch ID", String.valueOf(id));

                            OrderItemModel orderItemModel = new OrderItemModel(idItem, product_idProduct, (float) ItemPrice, ItemQuantity, order_idOrder, productName);

                            order_item_list.add(orderItemModel);
//                            Log.d("ORDER ITEM LIST: ", String.valueOf(order_item_list.get(i).getIdItem()));
                            temp_order_item.add(order_item_list.get(i));

                            OrderModel orderModel = new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idstore, name, temp_order_item);
                            temp_order.add(orderModel);

                            if((idOrder != temp_idOrder && idOrder != 0) || response.length() == 1 || i == response.length()-1){
                                Log.d("Inside If", String.valueOf(order_item_list.size()));
                                temp_order_item = new ArrayList<>();
                                for(int j=0; j < order_item_list.size(); j++){
                                    Log.d("Inside For", String.valueOf(order_item_list.size()));
                                    if((temp_idOrder == order_item_list.get(j).getOrder_idOrder()) || (response.length() == 1)){
                                        Log.d("Inside If", String.valueOf(order_item_list.size()));
                                        temp_order_item.add(order_item_list.get(j));
                                        Log.d("TEMP LIST: ", String.valueOf(i));
                                    }
                                }

//                            Log.d("TEMP LIST: ", temp_order_item.get(i).getProductName());
                                if(i != 0){
                                    if (temp_order.get(i-1).getStore_idstore() == id){
                                        OrderModel orderModel2 = new OrderModel(temp_order.get(i-1).getIdOrder(), temp_order.get(i-1).getOrderItemTotalPrice(), temp_order.get(i-1).getOrderStatus(), temp_order.get(i-1).getStore_idstore(), temp_order.get(i-1).getUsers_name(), temp_order_item);
                                        order_list.add(orderModel2);
                                    }
                                }
                                if(response.length()==1 || i == response.length()-1){
                                    if (i == response.length()-1 && temp_idOrder != idOrder && temp_order.get(i).getStore_idstore() == id){
                                        OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), Collections.singletonList(order_item_list.get(i)));
                                        order_list.add(orderModel2);
                                    }else{
                                        if (temp_order.get(i).getStore_idstore() == id){
                                            OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), temp_order_item);
                                            order_list.add(orderModel2);
                                        }

                                    }

                                }

                                Log.d("ORDER LIST: ", "Just added #" + i);
//                            if(i!=0){
//                                Log.d("ORDER MODEL: ", String.valueOf(order_list.get(i).getIdOrder()));
//                            }

                            }
                            if((orderStatus.equals("preparing") || orderStatus.equals("pending")) && store_idstore == id){
                                incoming_count++;
                                Log.d("Incoming Count", String.valueOf(incoming_count));
                            }
                            temp_idOrder = idOrder;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Order Size" , String.valueOf(temp_order.size()));
//                    if(i != 0){
//                        orderAdapter = new OrderAdapter(getActivity(), order_list, recyclerViewInterface);
//                        rv_orders.setAdapter(orderAdapter);
//                    }
                }
                tv_incoming_orders.setText(String.valueOf(incoming_count));
                innit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnError O: ", String.valueOf(error));
            }
        });
        requestQueue3.add(jsonArrayRequestRec3);
    }

    private void newCust(String storeId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "newCust.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    Log.d("NewCust: success= ", result);
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d("NewCust: Array Length ", String.valueOf(jsonArray.length()));

                    int newcust_count = 0;
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String orderStatus = jsonObject.getString("orderStatus");

                        if (orderStatus.equals("pending") || orderStatus.equals("preparing")){
                            newcust_count++;
                            Log.d("NewCust Count", String.valueOf(newcust_count));
                        }
                    }
                    Log.d("NewCust:", "Outside Loop");
                    tv_new_customers.setText(String.valueOf(newcust_count));
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    //For Dynamic Table
    public void innit(){
        Log.d("Innit", "inside");
        Log.d("Innit OL size", String.valueOf(order_list.size()));
//        TableLayout tl_dashboard  = (TableLayout) root.findViewById(R.id.tl_dashboard);
        for (int i=0;i<order_list.size();i++){
            Log.d("Innit OL size", String.valueOf(order_list.size()));
            //Creating Table Rows
            TableRow tbrow = new TableRow(getActivity());
            TextView tv_orderID = new TextView(getActivity());
            tv_orderID.setText(String.valueOf(i+1));
            tv_orderID.setTextColor(Color.BLACK);
            tv_orderID.setGravity(Gravity.CENTER);
            tv_orderID.setPadding(60,60,60,60);
            tbrow.addView(tv_orderID);

            TextView tv_custName = new TextView(getActivity());
            tv_custName.setText(order_list.get(i).getUsers_name());
            tv_custName.setTextColor(Color.BLACK);
            tv_custName.setGravity(Gravity.CENTER);
            tv_custName.setPadding(60,60,60,60);
            tbrow.addView(tv_custName);

            TextView tv_totalPrice = new TextView(getActivity());
            tv_totalPrice.setText(String.valueOf(order_list.get(i).getOrderItemTotalPrice()));
            tv_totalPrice.setTextColor(Color.BLACK);
            tv_totalPrice.setGravity(Gravity.CENTER);
            tv_totalPrice.setPadding(60,60,60,60);
            tbrow.addView(tv_totalPrice);

            TextView tv_viewDetails = new TextView(getActivity());
            tv_viewDetails.setText("thisbut");
            tv_viewDetails.setTextColor(Color.BLACK);
            tv_viewDetails.setGravity(Gravity.CENTER);
            tv_viewDetails.setPadding(60,60,60,60);
            tv_viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow row = (TableRow) view.getParent();
                    int index = tl_dashboard.indexOfChild(row) - 1;

                    Bundle order = new Bundle();
                    //bundle.putParcelableArrayList(order_list);
                    OrderSummaryFragment fragment = new OrderSummaryFragment();
                    //bundle.putSerializable("OrderSummary", order_list);
                    //order.putParcelableArrayList("OrderItemList", (ArrayList<? extends Parcelable>) order_list); //
                    order.putParcelable("Order",order_list.get(index));
                    fragment.setArguments(order);
                    Log.d("TAG", "Success");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                    Log.d("Innit But", String.valueOf(index));
                }
            });
            tbrow.addView(tv_viewDetails);

            Log.d("Innit TBrow", String.valueOf(tbrow.getChildCount()));

            tl_dashboard.addView(tbrow);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}