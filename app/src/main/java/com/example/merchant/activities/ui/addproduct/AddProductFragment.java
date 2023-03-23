package com.example.merchant.activities.ui.addproduct;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.RealPathUtil;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.databinding.FragmentAddProductBinding;
import com.example.merchant.models.IPModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductFragment extends Fragment {

    private FragmentAddProductBinding binding;
    EditText name_text_input, description_text_input, preptime_text_input,category_text_input, servesize_text_input, price_text_input;
    Button btn_add_product, btn_upload;
    ImageView iv_product_img;
    private static String JSON_URL;
    private IPModel ipModel;

    private String product_name, description, category, servesize, prep_time, prep_time_tmp, price_tmp, weather_tmp;
    float price = 0;
    String path;
    Bitmap bitmap;

    Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        preptime_text_input = root.findViewById(R.id.preptime_text_input);
        category_text_input = root.findViewById(R.id.category_spinner);
        servesize_text_input = root.findViewById(R.id.servesize_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_add_product = root.findViewById(R.id.btn_add_product);
        btn_upload = root.findViewById(R.id.btn_upload);
        iv_product_img = root.findViewById(R.id.iv_product_img);
        spinner = root.findViewById(R.id.weather_spinner);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(), R.array.weather, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

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

        binding.btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){

                    product_name = String.valueOf(name_text_input.getText());
                    description = String.valueOf(name_text_input.getText());
                    category = String.valueOf(category_text_input.getText());
                    servesize = String.valueOf(servesize_text_input.getText());
                    prep_time_tmp = String.valueOf(preptime_text_input.getText());
                    price_tmp = String.valueOf((price_text_input.getText()));
                    weather_tmp = spinner.getSelectedItem().toString().toLowerCase();

                    if (product_name.isEmpty() || description.isEmpty() || category.isEmpty() || servesize.isEmpty()
                            || prep_time_tmp.isEmpty() || price_tmp.isEmpty()){
                        if (product_name.isEmpty())
                            name_text_input.setError("Please insert Product Name!");
                        if (description.isEmpty())
                            description_text_input.setError("Please insert Description!");
                        if (category.isEmpty())
                            category_text_input.setError("Please insert Category!");
                        if (servesize.isEmpty())
                            servesize_text_input.setError("Please insert Serving Size!");
                        if (prep_time_tmp.isEmpty())
                            preptime_text_input.setError("Please insert Preparation Time!");
                        if (price_tmp.isEmpty())
                            price_text_input.setError("Please insert Price!");
                    } else {
                        prep_time = prep_time_tmp;
                        price = Float.parseFloat(price_tmp);
                    }

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final int idStore = 4;
                    final String pname = product_name;
                    final String pdesc = description;
                    final String ptag = category;
                    final String pservesize = servesize;
                    final String ppreptime = prep_time_tmp;
                    final float pprice = Float.parseFloat(price_tmp);
                    final String pweather = weather_tmp;


                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "add_prod.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String result) {
                                        Log.d("QueryResult", result );
                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            String success = jsonObject.getString("success");

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
                                Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> paramV = new HashMap<>();
                                paramV.put("idStore", String.valueOf(idStore));
                                paramV.put("productImage", base64Image);
                                paramV.put("productName", pname);
                                paramV.put("productDescription", pdesc);
                                paramV.put("productTag", ptag);
                                paramV.put("productServingSize", pservesize);
                                paramV.put("productPrice", String.valueOf(pprice));
                                paramV.put("productPrepTime", ppreptime);
                                paramV.put("weather", pweather);
                                return paramV;
                            }
                        };

                        queue.add(stringRequest);
                } else
                    Toast.makeText(getActivity().getApplicationContext(),"Please select an image first!", Toast.LENGTH_SHORT).show();

                ProductsFragment productsFragment = new ProductsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, productsFragment).commit();
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