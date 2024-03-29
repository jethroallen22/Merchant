package com.example.merchant.activities.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.MainActivity;
import com.example.merchant.activities.ui.addproduct.AddProductFragment;
import com.example.merchant.activities.ui.editproduct.EditProductFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.adapters.ProductAdapter;
import com.example.merchant.databinding.FragmentProductsBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFragment extends Fragment implements RecyclerViewInterface, ProductAdapter.OnItemClickListener {

    private FragmentProductsBinding binding;
    private RequestQueue requestQueue;
    private static String JSON_URL;
    private IPModel ipModel;
    //Product List Recycler View
    RecyclerView rv_products;
    List<ProductModel> product_list;
    ProductAdapter productAdapter;
    RecyclerViewInterface recyclerViewInterface;
    TextView tv_product_namee2, tv_product_pricee2, tv_product_description2, tv_product_info;

    ImageView iv_product_imagee2;
    Button btn_stock;
    String stockStatus;

    public static String name = "";
    public static String email = "";
    public static int id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            Log.d("Merchant", name + " " + id + " " + email);
        }

        rv_products = root.findViewById(R.id.rv_products);
        product_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                product_list = new ArrayList<>();
                extractFoodforyou();
                root.postDelayed(this, 15000);
            }
        }, 1000);


        binding.fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                AddProductFragment fragment = new AddProductFragment();
                fragment.setArguments(bundle);
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

    //Function
    //Display Product BottomSheet

    public void showBottomSheet(int position){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        iv_product_imagee2 = bottomSheetView.findViewById(R.id.iv_product_imagee2);
        tv_product_namee2 = bottomSheetView.findViewById(R.id.tv_product_namee2);
        tv_product_pricee2 = bottomSheetView.findViewById(R.id.tv_product_pricee2);
        tv_product_description2 = bottomSheetView.findViewById(R.id.tv_product_description2);
        tv_product_info = bottomSheetView.findViewById(R.id.tv_product_info);
        btn_stock = bottomSheetView.findViewById(R.id.btn_stock);

        iv_product_imagee2.setImageBitmap(product_list.get(position).getBitmapImage());
        tv_product_namee2.setText(product_list.get(position).getProductName());
        tv_product_pricee2.setText("P "+ product_list.get(position).getProductPrice());
        tv_product_description2.setText(product_list.get(position).getProductDescription());
        tv_product_info.setText(product_list.get(position).getProductPrepTime() +
                "min | " + product_list.get(position).getProductServingSize() +
                " | " + product_list.get(position).getProductTag());
        if(product_list.get(position).getStock().equalsIgnoreCase("stocked")){
            btn_stock.setText("STOCKED");
            btn_stock.setTextColor(Color.WHITE);
            btn_stock.setBackgroundColor(getResources().getColor(R.color.mosibusPrimary));
        } else {
            btn_stock.setText("OUT OF\nSTOCK");
            btn_stock.setBackgroundColor(Color.RED);
        }

        btn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Stock Button pos", String.valueOf(position));
//                Log.d("Stock Button", String.valueOf(product_list.get(position).getIdProduct()));
                if (product_list.get(position).getStock().equalsIgnoreCase("stocked")){
                    stockStatus = "out";
                } else stockStatus = "stocked";
                stockProduct(position);
            }
        });
        Log.d(TAG,"bottomSheetView = LayoutInflater.from");
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void extractFoodforyou(){
        ProductsFragment productsFragment = this;
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"apifood.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
                        Log.d("IDSTORE: ", String.valueOf(jsonObjectFoodforyou.getInt("idStore")));
                        Log.d("ID: " , String.valueOf(id));
                        if (jsonObjectFoodforyou.getInt("idStore") == id) {
                            int idProduct = jsonObjectFoodforyou.getInt("idProduct");
                            int idStore = jsonObjectFoodforyou.getInt("idStore");
                            String productName = jsonObjectFoodforyou.getString("productName");
                            String productDescription = jsonObjectFoodforyou.getString("productDescription");
                            float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                            String productImage = jsonObjectFoodforyou.getString("productImage");
                            String productServingSize = jsonObjectFoodforyou.getString("productServingSize");
                            String productTag = jsonObjectFoodforyou.getString("productTag");
                            String productPrepTime = jsonObjectFoodforyou.getString("productPrepTime");
                            String storeName = jsonObjectFoodforyou.getString("storeName");
                            String storeImage = jsonObjectFoodforyou.getString("storeImage");
                            String weather = jsonObjectFoodforyou.getString("weather");
                            String stock = jsonObjectFoodforyou.getString("stock");

                            ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                    productServingSize, productTag, productPrepTime, storeName, storeImage, weather, stock);
                            product_list.add(foodfyModel);
                            //list.add(productName);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productAdapter = new ProductAdapter(getActivity(),product_list,ProductsFragment.this);
                    rv_products.setAdapter(productAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_products.setLayoutManager(layoutManager);

                    //Remove Product
                    productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.d("Product List size", String.valueOf(product_list.size()));
                            Log.d("Position", String.valueOf(position));
//                            product_list.remove(position);
//                            productAdapter.notifyItemRemoved(position);
                            deleteProduct(position);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);
    }

    public void deleteProduct(int position){
        Log.d("Delete Product", product_list.get(position).getProductName());
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "deleteProd.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("On Res", "inside on res");
                        Toast.makeText(getActivity(), "Your Product has been Removed", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idProduct", String.valueOf(product_list.get(position).getIdProduct()));
                Log.d("PARAM", product_list.get(position).getProductName());
                return paramV;
            }
        };
        queue.add(stringRequest);
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("id", id);
        bundle.putString("email", email);
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }

    public void stockProduct(int position){
//        Log.d("Stock Product", (String) btn_stock.);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "stock.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("On Res", "inside on res");
//                        Toast.makeText(getActivity(), "Your Product has been Removed", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idProduct", String.valueOf(product_list.get(position).getIdProduct()));
                paramV.put("stockStatus", stockStatus);
                Log.d("PARAM", product_list.get(position).getProductName());
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
    @Override
    public void onItemClick(int position) {
        showBottomSheet(position);
    }

    @Override
    public void onItemClickEdit(int position){
        Log.d("Test", "Success");
        EditProductFragment fragment = new EditProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Product", product_list.get(position));
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }
}