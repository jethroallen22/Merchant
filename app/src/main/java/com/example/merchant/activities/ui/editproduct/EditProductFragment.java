package com.example.merchant.activities.ui.editproduct;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.RealPathUtil;
import com.example.merchant.activities.ui.Dashboard.DashboardViewModel;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.databinding.FragmentDashboardBinding;
import com.example.merchant.databinding.FragmentEditProductBinding;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProductFragment extends Fragment {

    private FragmentEditProductBinding binding;

    private static String JSON_URL = "http://10.154.162.184/mosibus_php/merchant/";

    EditText name_text_input, description_text_input, preptime_text_input,category_text_input, servesize_text_input, price_text_input;
    Button btn_edit_product, btn_upload;
    ImageView iv_product_img;

    private String product_name, description, category, servesize, prep_time_tmp, price_tmp;
    private int prep_time = 0;
    private float price = 0;
    private int idProduct, idStore;
    private ProductModel productModel;
    Bitmap bitmap;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productModel = new ProductModel();
            productModel = bundle.getParcelable("Product");
            idProduct = productModel.getIdProduct();
            idStore = productModel.getStore_idStore();
            Log.d("IDPRODUCT" , String.valueOf(idProduct) + " " + String.valueOf(idStore));
        }

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        preptime_text_input = root.findViewById(R.id.preptime_text_input);
        category_text_input = root.findViewById(R.id.category_text_input);
        servesize_text_input = root.findViewById(R.id.servesize_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_edit_product = root.findViewById(R.id.btn_edit_product);
        btn_upload = root.findViewById(R.id.btn_upload);
        iv_product_img = root.findViewById(R.id.iv_edit_product_img);


        name_text_input.setText(productModel.getProductName());
        description_text_input.setText(productModel.getProductDescription());
        preptime_text_input.setText(String.valueOf(productModel.getProductPrepTime()));
        category_text_input.setText(productModel.getProductTag());
        servesize_text_input.setText(productModel.getProductServingSize());
        price_text_input.setText(String.valueOf(productModel.getProductPrice()));

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_product_img.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        btn_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////////////// UPDATE DB//////////////////
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){

                    product_name = String.valueOf(name_text_input.getText());
                    description = String.valueOf(name_text_input.getText());
                    category = String.valueOf(category_text_input.getText());
                    servesize = String.valueOf(servesize_text_input.getText());
                    prep_time_tmp = String.valueOf(preptime_text_input.getText());
                    price_tmp = String.valueOf(price_text_input.getText());

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final int pid = idProduct;
                    final int psid = idStore;
                    final String pname = product_name;
                    final String pdesc = description;
                    final String ptag = category;
                    final String pservesize = servesize;
                    final String ppreptime = prep_time_tmp;
                    final Float pprice = Float.valueOf(price_tmp);

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "editProd.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String result) {
                                    Log.d("InsideEdit", String.valueOf(idProduct));
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String success = jsonObject.getString("success");
                                        Log.d("InsideEdit", success);

                                        if (success.equals("1")){
                                            Log.d("TEMP CART INSERT", "success");
                                        } else {
                                            Toast.makeText(getContext(), "Not Inserted",Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Log.d("TEMP CART", "catch" );
//                                            Toast.makeText(getContext(), "Catch ",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("idProduct", String.valueOf(pid));
                            paramV.put("idStore", String.valueOf(psid));
                            paramV.put("productImage", base64Image);
                            paramV.put("productName", pname);
                            paramV.put("productDescription", pdesc);
                            paramV.put("productTag", ptag);
                            paramV.put("productServingSize", pservesize);
                            paramV.put("productPrice", String.valueOf(pprice));
                            paramV.put("productPrepTime", ppreptime);
                            Log.d("PARAM", pid + " " + pname);
                            return paramV;
                        }
                    };

                    queue.add(stringRequest);

                    Bundle bundle = new Bundle();
                    ProductsFragment fragment = new ProductsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();

                } else
                    Toast.makeText(getActivity().getApplicationContext(),"Please select an image first!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Context context = getActivity();
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            iv_product_img.setImageBitmap(bitmap);
            Log.d("IMAGE" , String.valueOf(iv_product_img.getDrawable()));
        }
    }

}