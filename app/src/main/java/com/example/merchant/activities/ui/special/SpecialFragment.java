package com.example.merchant.activities.ui.special;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.merchant.activities.ui.addproduct.AddProductFragment;
import com.example.merchant.activities.ui.editproduct.EditProductFragment;
import com.example.merchant.activities.ui.slideshow.ProductsViewModel;
import com.example.merchant.adapters.ProductAdapter;
import com.example.merchant.adapters.SpecialAdapter;
import com.example.merchant.databinding.FragmentProductsBinding;
import com.example.merchant.databinding.FragmentSpecialBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialFragment extends Fragment implements RecyclerViewInterface, SpecialAdapter.OnItemClickListener {

    private FragmentSpecialBinding binding;
    private RequestQueue requestQueue;
    private static String JSON_URL;
    private IPModel ipModel;
    //Product List Recycler View
    RecyclerView rv_special;
    List<ProductModel> product_list, applied;
    SpecialAdapter specialAdapter;
    RecyclerViewInterface recyclerViewInterface;
    TextView tv_product_namee2, tv_product_pricee2, tv_product_description2, tv_product_info,
             tv_special_name, tv_special_desc;

    ImageView iv_product_imagee2;

    public static String name = "";
    public static String email = "";
    public static int id;
    Button btn_add_special;
    CheckBox checkBoxProd;
    List<Integer> checkedList;
    String title, description, specialTagNot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        ProductsViewModel productsViewModel =
//                new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentSpecialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            applied = bundle.getParcelableArrayList("applied");
            Log.d("Merchant", name + " " + id + " " + email);
        }


        rv_special = root.findViewById(R.id.rv_special);
        btn_add_special = root.findViewById(R.id.btn_add_special);
        checkBoxProd = root.findViewById(R.id.checkBoxProd);
        tv_special_name = root.findViewById(R.id.tv_special_name);
        tv_special_desc = root.findViewById(R.id.tv_special_desc);
//        product_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();

//        root.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                product_list = new ArrayList<>();
//                extractFoodforyou();
//                root.postDelayed(this, 10000);
//            }
//        }, 0);

        product_list = new ArrayList<>();
        checkedList = new ArrayList<>();
        extractFoodforyou();
        getNotif();


        btn_add_special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkedList.isEmpty()){
                    addSpecial(checkedList);
                    Log.d("Button", "Sent to DB");
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putInt("id", id);
                    bundle.putString("email", email);
                    SpecialStatusFragment fragment = new SpecialStatusFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
                }
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

        iv_product_imagee2.setImageBitmap(product_list.get(position).getBitmapImage());
        tv_product_namee2.setText(product_list.get(position).getProductName());
        tv_product_pricee2.setText("P "+ product_list.get(position).getProductPrice());
        tv_product_description2.setText(product_list.get(position).getProductDescription());
        tv_product_info.setText(product_list.get(position).getProductPrepTime() +
                "min | " + product_list.get(position).getProductServingSize() +
                " | " + product_list.get(position).getProductTag());
        Log.d(TAG,"bottomSheetView = LayoutInflater.from");
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void extractFoodforyou(){
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

                            ProductModel foodfyModel = new ProductModel(idProduct, idStore, productName, productDescription, productPrice, productImage,
                                    productServingSize, productTag, productPrepTime, storeName, storeImage, weather);
                            if (applied != null){
                                for (int j=0;j<applied.size();j++){
                                    if (applied.get(j).getIdProduct() != foodfyModel.getIdProduct()){
                                        if (applied.size()-1 == j){
                                            product_list.add(foodfyModel);
                                        }
                                    }else break;
                                }
                            } else {
                                product_list.add(foodfyModel);
                            }


                            //list.add(productName);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    specialAdapter = new SpecialAdapter(getActivity(),product_list,SpecialFragment.this);
                    rv_special.setAdapter(specialAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_special.setLayoutManager(layoutManager);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);
    }

    public void addSpecial(List<Integer> checkedList){
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "addSpecial.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Log.d("On Res", "inside on res");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", String.valueOf(error));
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("idStore", String.valueOf(product_list.get(0).getStore_idStore()));
                paramV.put("specialTag", "Halal");//get from notif
                paramV.put("status", "pending");
                Gson gson = new Gson();
                String jsonArray = gson.toJson(checkedList);
                paramV.put("data", jsonArray);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void getNotif(){
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"specialNotif.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectNotif = response.getJSONObject(i);
//                        if (jsonObjectFoodforyou.getInt("idStore") == id) {
                            title = jsonObjectNotif.getString("title");
                            description = jsonObjectNotif.getString("description");
                            specialTagNot = jsonObjectNotif.getString("specialTag");

//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tv_special_name.setText(specialTagNot);
                    tv_special_desc.setText(description);
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
    public void onItemClick(int position) {
//        showBottomSheet(position);
        Log.d("Checkbox", product_list.get(position).getProductName());
        if (checkedList.size() == 0){
            checkedList.add(product_list.get(position).getIdProduct());
        } else {
            Log.d("Checkbox", "inside else");
            for (int i=0;i<checkedList.size();i++){
                if(checkedList.get(i) == product_list.get(position).getIdProduct()){
                    checkedList.remove(i);
                    Log.d("Checkbox", "removed: " + product_list.get(position).getProductName());
                    break;
                }else if (i == checkedList.size()-1){
                    checkedList.add(product_list.get(position).getIdProduct());
                    Log.d("Checkbox", "added: " + product_list.get(position).getProductName());
                    break;
                }
            }
        }
    }

    @Override
    public void onItemClickEdit(int position){
//        Log.d("Test", "Success");
//        EditProductFragment fragment = new EditProductFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("Product", product_list.get(position));
//        fragment.setArguments(bundle);
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }
}