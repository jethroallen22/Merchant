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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOrdersBinding binding;
    //Cart List Recycler View
    RecyclerView rv_orders;
    List<OrderModel> orderModelList;
    List<OrderModel> temp_order;
    OrderAdapter orderAdapter;
    List<OrderItemModel> order_item_list;
    List<OrderItemModel> temp_order_item;
    List<ProductModel> product_list;
    OrderItemsAdapter orderItemsAdapter;
    RecyclerViewInterface recyclerViewInterface = this;
    private RequestQueue requestQueue1, requestQueue2, requestQueue3, requestQueue4, requestQueue5;
    private static String JSON_URL;
    private IPModel ipModel;
    int temp_idOrder = 0;

    public static String name = "";
    public static String email = "";
    public static int id;

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
        requestQueue1 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue2 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue3 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue4 = Singleton.getsInstance(getActivity()).getRequestQueue();
        requestQueue5 = Singleton.getsInstance(getActivity()).getRequestQueue();
        Log.d("Start ", "Before ExtractOrderItem");
        product_list = new ArrayList<>();
        order_item_list = new ArrayList<>();
        orderModelList = new ArrayList<>();
        temp_order_item = new ArrayList<>();
        temp_order = new ArrayList<>();
        final List<OrderModel> order_list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequestOrder = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apiorderget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseLength", String.valueOf(response));
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObjectOrder = response.getJSONObject(i);
                        int idOrder = jsonObjectOrder.getInt("idOrder");
                        float orderItemTotalPrice = jsonObjectOrder.getInt("orderItemTotalPrice");
                        String orderStatus = jsonObjectOrder.getString("orderStatus");
                        int store_idStore = jsonObjectOrder.getInt("store_idStore");
                        String name = jsonObjectOrder.getString("name");
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

        //Log.d("Size", String.valueOf(order_item_list.size()));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {

//        Log.d("TAG", "Success");
//        Bundle bundle = new Bundle();
//        //bundle.putInt("StoreImage", home_pop_store_list.get(position).getStore_image());
//        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
//        bundle.putString("StoreAddress", "Esterling Heights Subdivision, Guintorilan City");
//        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());
        Log.d("Test", "Click");
        Bundle order = new Bundle();
        //bundle.putParcelableArrayList(order_list);
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        //bundle.putSerializable("OrderSummary", order_list);
        //order.putParcelableArrayList("OrderItemList", (ArrayList<? extends Parcelable>) order_list); //
        order.putParcelable("Order",orderModelList.get(position));
        fragment.setArguments(order);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("TAG", "Success Click");
    }

    @Override
    public void onItemClickEdit(int position) {

    }

    //PRODUCT DB
    public void extractProduct(){
        Log.d("JSON_URL_MERCHANT: ", JSON_URL);

        JsonArrayRequest jsonArrayRequestRec1 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "testP.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response Product: ", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        Log.d("Try P: ", "Im in");
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);

                        //PRODUCT DB
                        int idProduct = jsonObjectRec1.getInt("idProduct");
                        int store_idStore = jsonObjectRec1.getInt("store_idStore");
                        String productName = jsonObjectRec1.getString("productName");
                        String productDescription = jsonObjectRec1.getString("productDescription");
                        float productPrice = (float) jsonObjectRec1.getDouble("productPrice");
                        String productImage = jsonObjectRec1.getString("productImage");
                        String productServingSize = jsonObjectRec1.getString("productServingSize");
                        String productTag = jsonObjectRec1.getString("productTag");
                        int productPrepTime = jsonObjectRec1.getInt("productPrepTime");

                        //ProductModel productModel = new ProductModel(idProduct, store_idStore, productName, productDescription, (float) productPrice, productImage, productServingSize, productTag, productPrepTime);

                        //product_list.add(productModel);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Product Size" , String.valueOf(product_list.size()));

//                    orderAdapter = new OrderAdapter(getActivity(),order_item_list);
//                    rv_home_store_rec.setAdapter(homeStoreRecAdapter);

                }
            }
//            public Request.Priority Priority(){
//                return Request.Priority.HIGH;
//            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnError P: ", String.valueOf(error));
            }
        });
        requestQueue1.add(jsonArrayRequestRec1);
    }

    //OrderItem DB
    public void extractOrderItem(){
        Log.d("JSON_URL_MERCHANT: ", JSON_URL);

        JsonArrayRequest jsonArrayRequestRec2 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "testOI.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response OrderItem: ", String.valueOf(response.length()));
                for (int i=0; i < response.length(); i++){
                    try {
                        Log.d("Try OI: ", "Im in");
                        JSONObject jsonObjectRec1 = response.getJSONObject(i);

                        //ORDERITEM DB
                        int idItem = jsonObjectRec1.getInt("idItem");
                        Log.d("Test", String.valueOf(idItem));
                        int product_idProduct = jsonObjectRec1.getInt("product_idProduct");
                        float ItemPrice = (float) jsonObjectRec1.getDouble("ItemPrice");
                        int ItemQuantity = jsonObjectRec1.getInt("ItemQuantity");
                        int order_idOrder = jsonObjectRec1.getInt("order_idOrder");
//                        Log.d("OrderItem: ", "after variables");
//                        String temp = product_list.get(i).getProductName();
//                        Log.d("OrderItem ProductName: ", temp);
                        String productName = "";
                        for (int j = 0 ; j < product_list.size() ; j++){
                            Log.d("ProdID", String.valueOf(product_idProduct));
                            Log.d("ProdProdID",product_list.get(j).getProductName());
                            if(product_idProduct == product_list.get(j).getIdProduct()){
                                productName = product_list.get(j).getProductName();
                                Log.d("ProdSucc","Nice");
                            }
                        }
                        OrderItemModel orderItemModel = new OrderItemModel(product_idProduct, (float) ItemPrice, ItemQuantity, order_idOrder, productName);

                        order_item_list.add(orderItemModel);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("OrderItem Size" , String.valueOf(order_item_list.size()));

//                    orderAdapter = new OrderAdapter(getActivity(),order_item_list);
//                    rv_home_store_rec.setAdapter(homeStoreRecAdapter);

                }
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OnError OI: ", String.valueOf(error));
            }
        });
        requestQueue1.add(jsonArrayRequestRec2);
    }

    //Order DB
//    public void extractOrder(){
//
//        JsonArrayRequest jsonArrayRequestRec3 = new JsonArrayRequest(Request.Method.GET, JSON_URL_MERCHANT + "testO.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.d("Response Order: ", String.valueOf(response.length()));
//                for (int i=0; i < response.length(); i++){
//                    try {
//                        Log.d("Try O: ", "Im in");
//                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
//                        //ORDER DB
//                        int idOrder = jsonObjectRec1.getInt("idOrder");
//                        int orderItemTotalPrice = jsonObjectRec1.getInt("orderItemTotalPrice");
//                        String orderStatus = jsonObjectRec1.getString("orderStatus");
//                        int store_idstore = jsonObjectRec1.getInt("store_idstore");
//                        int users_id = jsonObjectRec1.getInt("users_id");
//
//                        temp_order_item = new ArrayList<>();
//                        for(int j=0; j < order_item_list.size(); j++){
//                            if(idOrder == order_item_list.get(j).getOrder_idOrder()){
//                                temp_order_item.add(order_item_list.get(j));
//                                Log.d("TEMP LIST: ", String.valueOf(i));
//                            }
//                        }
//                        OrderModel orderModel = new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idstore, users_id, temp_order_item);
//
//                        order_list.add(orderModel);
//                        Log.d("ORDER LIST: ", "Just added #" + i);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Order Size" , String.valueOf(order_list.size()));
//                    orderAdapter = new OrderAdapter(getActivity(), order_list, recyclerViewInterface);
//                    rv_orders.setAdapter(orderAdapter);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("OnError O: ", String.valueOf(error));
//            }
//        });
//        requestQueue3.add(jsonArrayRequestRec3);
//    }

    //Order DB
//    public void extractOrders(){
//        Log.d("extractOrders", "Called");
//        JsonArrayRequest jsonArrayRequestRec3 = new JsonArrayRequest(Request.Method.GET, JSON_URL + "testAll.php", null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.e("Bug", String.valueOf(response));
//                Log.d("Response Order: ", String.valueOf(response.length()));
//                for (int i=0; i < response.length(); i++){
//                    try {
//                        Log.d("Try O: ", "Im in");
//                        JSONObject jsonObjectRec1 = response.getJSONObject(i);
//
//                        //USERS DB
//                        String name = jsonObjectRec1.getString("name");
//
//                        //ORDER DB
//                        int idOrder = jsonObjectRec1.getInt("idOrder");
//                        int orderItemTotalPrice = jsonObjectRec1.getInt("orderItemTotalPrice");
//                        String orderStatus = jsonObjectRec1.getString("orderStatus");
//                        int store_idstore = jsonObjectRec1.getInt("store_idstore");
//
//                        //OrderItem DB
//                        int idItem = jsonObjectRec1.getInt("idItem");
//                        Log.d("Test", String.valueOf(idItem));
//                        int product_idProduct = jsonObjectRec1.getInt("product_idProduct");
//                        int order_idOrder = jsonObjectRec1.getInt("order_idOrder");
//                        float ItemPrice = (float) jsonObjectRec1.getDouble("ItemPrice");
//                        int ItemQuantity = jsonObjectRec1.getInt("ItemQuantity");
//
//                        //PRODUCT DB
//                        String productName = jsonObjectRec1.getString("productName");
//
//                            OrderItemModel orderItemModel = new OrderItemModel(product_idProduct, (float) ItemPrice, ItemQuantity, order_idOrder, productName);
//
//                            order_item_list.add(orderItemModel);
////                            Log.d("ORDER ITEM LIST: ", String.valueOf(order_item_list.get(i).getIdItem()));
//                            temp_order_item.add(order_item_list.get(i));
//
//                            OrderModel orderModel = new OrderModel(idOrder, orderItemTotalPrice, orderStatus, store_idstore, name, temp_order_item);
//                            temp_order.add(orderModel);
//
//                            if((idOrder != temp_idOrder && idOrder != 0) || response.length() == 1 || i == response.length()-1){
//                                Log.d("Inside If", String.valueOf(order_item_list.size()));
//                                temp_order_item = new ArrayList<>();
//                                for(int j=0; j < order_item_list.size(); j++){
//                                    Log.d("Inside For", String.valueOf(order_item_list.size()));
//                                    if((temp_idOrder == order_item_list.get(j).getIdOrder()) || (response.length() == 1)){
//                                        Log.d("Inside If", String.valueOf(order_item_list.size()));
//                                        temp_order_item.add(order_item_list.get(j));
//                                        Log.d("TEMP LIST: ", String.valueOf(i));
//                                    }
//                                }
//
////                            Log.d("TEMP LIST: ", temp_order_item.get(i).getProductName());
//                                if(i != 0){
//                                    if (temp_order.get(i-1).getStore_idstore() == id){
//                                        OrderModel orderModel2 = new OrderModel(temp_order.get(i-1).getIdOrder(), temp_order.get(i-1).getOrderItemTotalPrice(), temp_order.get(i-1).getOrderStatus(), temp_order.get(i-1).getStore_idstore(), temp_order.get(i-1).getUsers_name(), temp_order_item);
//                                        order_list.add(orderModel2);
//                                    }
//                                }
//                                if(response.length()==1 || i == response.length()-1){
//                                    if (i == response.length()-1 && temp_idOrder != idOrder && temp_order.get(i).getStore_idstore() == id){
//                                        OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), Collections.singletonList(order_item_list.get(i)));
//                                        order_list.add(orderModel2);
//                                    }else{
//                                        if (temp_order.get(i).getStore_idstore() == id){
//                                            OrderModel orderModel2 = new OrderModel(temp_order.get(i).getIdOrder(), temp_order.get(i).getOrderItemTotalPrice(), temp_order.get(i).getOrderStatus(), temp_order.get(i).getStore_idstore(), temp_order.get(i).getUsers_name(), temp_order_item);
//                                            order_list.add(orderModel2);
//                                        }
//
//                                    }
//
//                                }
//                                Log.d("ORDER LIST: ", "Just added #" + i);
////                            if(i!=0){
////                                Log.d("ORDER MODEL: ", String.valueOf(order_list.get(i).getIdOrder()));
////                            }
//
//                            }
//                            temp_idOrder = idOrder;
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Order Size" , String.valueOf(temp_order.size()));
//                    if(i != 0){
//                        orderAdapter = new OrderAdapter(getActivity(), order_list, recyclerViewInterface);
//                        rv_orders.setAdapter(orderAdapter);
//                    }
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("OnError O: ", String.valueOf(error));
//            }
//        });
//        requestQueue3.add(jsonArrayRequestRec3);
//    }

    public void extractOrder(){
        final List<OrderModel> order_list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequestOrder = new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifood.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseLength", String.valueOf(response.length()));
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObjectOrder = response.getJSONObject(i);
                        int idOrder = jsonObjectOrder.getInt("idOrder");
                        float orderItemTotalPrice = jsonObjectOrder.getInt("orderItemTotalPrice");
                        String orderStatus = jsonObjectOrder.getString("orderStatus");
                        int store_idStore = jsonObjectOrder.getInt("store_idStore");
                        String name = jsonObjectOrder.getString("name");
                        if (jsonObjectOrder.getInt("idStore") == id) {
                            final OrderModel tempOrderModel = new OrderModel();
                            tempOrderModel.setIdOrder(idOrder);
                            tempOrderModel.setOrderItemTotalPrice(orderItemTotalPrice);
                            tempOrderModel.setOrderStatus(orderStatus);
                            tempOrderModel.setStore_idstore(store_idStore);
                            tempOrderModel.setUsers_name(name);

                            JsonArrayRequest jsonArrayRequestOrderItemList = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apiorderitemget.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    Log.d("ResponseItemLength", String.valueOf(response.length()));
                                    List<OrderItemModel> orderItemModels = new ArrayList<>();
                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject jsonObjectOrderItemList = response.getJSONObject(i);
                                            if (jsonObjectOrderItemList.getInt("idOrder") == idOrder) {
                                                int idProduct = jsonObjectOrderItemList.getInt("idProduct");
                                                int idStore = jsonObjectOrderItemList.getInt("idStore");
                                                int idUser = jsonObjectOrderItemList.getInt("idUser");
                                                int idOrder = jsonObjectOrderItemList.getInt("idOrder");
                                                String productName = jsonObjectOrderItemList.getString("productName");
                                                int itemPrice = jsonObjectOrderItemList.getInt("itemPrice");
                                                int itemQuantity = jsonObjectOrderItemList.getInt("itemQuantity");
                                                float totalPrice = jsonObjectOrderItemList.getInt("totalPrice");

                                                OrderItemModel orderItemModel = new OrderItemModel(idProduct, idStore, idUser, idOrder, productName, itemPrice, itemQuantity, totalPrice);
                                                orderItemModels.add(orderItemModel);
                                            }
                                        }
                                        tempOrderModel.setOrderItem_list(orderItemModels);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            requestQueue5.add(jsonArrayRequestOrderItemList);
                            order_list.add(tempOrderModel);
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                orderModelList = order_list;
                orderAdapter = new OrderAdapter(getActivity(),orderModelList, OrdersFragment.this);
                rv_orders.setAdapter(orderAdapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rv_orders.setLayoutManager(layoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue4.add(jsonArrayRequestOrder);




    }
}