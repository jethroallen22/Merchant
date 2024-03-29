package com.example.merchant.activities.ui.deals;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.merchant.activities.ui.special.SpecialFragment;
import com.example.merchant.adapters.SpecialStatusAdapter;
import com.example.merchant.databinding.FragmentDealsStatusBinding;
import com.example.merchant.databinding.FragmentSpecialStatusBinding;
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

public class DealsStatusFragment extends Fragment implements RecyclerViewInterface, SpecialStatusAdapter.OnItemClickListener {

    private FragmentDealsStatusBinding binding;
    private RequestQueue requestQueue;
    private static String JSON_URL;
    private IPModel ipModel;
    //Product List Recycler View
    RecyclerView rv_deals_status;
    List<ProductModel> product_list;
    SpecialStatusAdapter specialStatusAdapter;
    RecyclerViewInterface recyclerViewInterface;
    TextView tv_product_namee2, tv_product_pricee2, tv_product_description2, tv_product_info,
             tv_special_name, tv_special_desc, tv_special_status;

    ImageView iv_product_imagee2;

    public static String name = "";
    public static String email = "";
    public static int id;
    Button btn_apply;
//    CheckBox checkBoxProd;
//    List<Integer> checkedList;
    String title, description, specialTagNot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDealsStatusBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            Log.d("Merchant Special Stat", name + " " + id + " " + email);
        }

        rv_deals_status = root.findViewById(R.id.rv_deals_status);
//        btn_apply = root.findViewById(R.id.btn_apply);
        tv_special_status = root.findViewById(R.id.tv_special_status);
        tv_special_name = root.findViewById(R.id.tv_special_name);
        tv_special_desc = root.findViewById(R.id.tv_special_desc);
//        product_list = new ArrayList<>();
        requestQueue = Singleton.getsInstance(getActivity()).getRequestQueue();

        root.postDelayed(new Runnable() {
            @Override
            public void run() {
                product_list = new ArrayList<>();
                extractFoodforyou();
                root.postDelayed(this, 5000);
            }
        }, 0);



        binding.fabAddDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                DealsFragment fragment = new DealsFragment();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                if (product_list.size() != 0){
                    bundle.putParcelableArrayList("applied", (ArrayList<ProductModel>) product_list);
                }
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
        JsonArrayRequest jsonArrayRequestFoodforyou= new JsonArrayRequest(Request.Method.GET, JSON_URL+"addDeals.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Special Stat: ", "inside onResponse");
                for (int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonObjectFoodforyou = response.getJSONObject(i);
//                        Log.d("IDSTORE special stat: ", String.valueOf(jsonObjectFoodforyou.getInt("idStore")));
//                        Log.d("ID special stat: " , String.valueOf(id));
                        if (jsonObjectFoodforyou.getInt("storeId") == id) {
                            Log.d("Deal Stat: ", String.valueOf(response));
                            int idProduct = jsonObjectFoodforyou.getInt("productId");
                            String productName = jsonObjectFoodforyou.getString("productName");
                            String productDescription = jsonObjectFoodforyou.getString("productDescription");
                            float productPrice = (float) jsonObjectFoodforyou.getDouble("productPrice");
                            String productImage = jsonObjectFoodforyou.getString("productImage");
                            int percentage = jsonObjectFoodforyou.getInt("percentage");
                            String status = jsonObjectFoodforyou.getString("status");

                            ProductModel foodfyModel = new ProductModel(idProduct, productName, productDescription, productPrice, productImage, status, percentage);
                            product_list.add(foodfyModel);
                            Log.d("Deals Stat: ", product_list.get(i).getProductName());
                            //list.add(productName);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    specialStatusAdapter = new SpecialStatusAdapter(getActivity(),product_list, DealsStatusFragment.this);
                    rv_deals_status.setAdapter(specialStatusAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    rv_deals_status.setLayoutManager(layoutManager);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequestFoodforyou);
    }

    @Override
    public void onItemClick(int position) {

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