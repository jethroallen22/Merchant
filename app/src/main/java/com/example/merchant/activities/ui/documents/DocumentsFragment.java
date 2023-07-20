package com.example.merchant.activities.ui.documents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.RealPathUtil;
import com.example.merchant.activities.ui.Dashboard.DashboardFragment;
import com.example.merchant.activities.ui.Dashboard.DashboardViewModel;
import com.example.merchant.databinding.FragmentDocumentsBinding;
import com.example.merchant.interfaces.Singleton;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentsFragment extends Fragment {
    private FragmentDocumentsBinding binding;
    private static String JSON_URL;
    private IPModel ipModel;
    ImageView iv_valid_id_placeholder, iv_mayors_permit_placeholder, iv_bir_permit_placeholder, iv_dti_permit_placeholder, iv_sanitary_permit_placeholder;
    CardView cv_upload_id, cv_upload_mayor, cv_upload_bir, cv_upload_dti, cv_upload_sanitary;
    Button btn_save_edit;
    Bitmap bitmap_valid_id, bitmap_mayor, bitmap_bir, bitmap_dti, bitmap_sanitary;
    String path;
    int id;
    RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDocumentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if(bundle != null){
            id = bundle.getInt("id");
            Log.d("idStoreDocument", String.valueOf(id));
        }

        iv_valid_id_placeholder = root.findViewById(R.id.iv_valid_id_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherValidId = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap_valid_id = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_valid_id_placeholder.setImageBitmap(bitmap_valid_id);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_id = root.findViewById(R.id.cv_upload_id);
        cv_upload_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherValidId.launch(intent);
            }
        });

        iv_mayors_permit_placeholder = root.findViewById(R.id.iv_mayors_permit_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherMayor = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap_mayor = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_mayors_permit_placeholder.setImageBitmap(bitmap_mayor);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_mayor = root.findViewById(R.id.cv_upload_mayor);
        cv_upload_mayor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherMayor.launch(intent);
            }
        });

        iv_bir_permit_placeholder = root.findViewById(R.id.iv_bir_permit_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherBir = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap_bir = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_bir_permit_placeholder.setImageBitmap(bitmap_bir);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_bir = root.findViewById(R.id.cv_upload_bir);
        cv_upload_bir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherBir.launch(intent);
            }
        });

        iv_dti_permit_placeholder = root.findViewById(R.id.iv_dti_permit_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherDti = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap_dti = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_dti_permit_placeholder.setImageBitmap(bitmap_dti);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_dti = root.findViewById(R.id.cv_upload_dti);
        cv_upload_dti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherDti.launch(intent);
            }
        });

        iv_sanitary_permit_placeholder = root.findViewById(R.id.iv_sanitary_permit_placeholder);
        ActivityResultLauncher<Intent> activityResultLauncherSanitary = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap_sanitary = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_sanitary_permit_placeholder.setImageBitmap(bitmap_sanitary);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        cv_upload_sanitary = root.findViewById(R.id.cv_upload_sanitary);
        cv_upload_sanitary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherSanitary.launch(intent);
            }
        });

        readDocuments();

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        btn_save_edit = root.findViewById(R.id.btn_save_edit);
        btn_save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define arrays to store bitmaps and image names
                Bitmap[] bitmaps = {bitmap_valid_id, bitmap_mayor, bitmap_bir, bitmap_dti, bitmap_sanitary};
                String[] imageNames = {"validId", "permitMayor", "permitBir", "permitDti", "permitSanitary"};
                String[] imageBase64 = new String[5];

// Iterate through the bitmaps and process them
                for (int i = 0; i < bitmaps.length; i++) {
                    Bitmap bitmap = bitmaps[i];
                    if (bitmap != null) {
                        // Create a new ByteArrayOutputStream for each iteration
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        imageBase64[i] = Base64.encodeToString(bytes, Base64.DEFAULT);
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Please upload a valid " + imageNames[i], Toast.LENGTH_SHORT).show();
                        return; // Stop the execution if any image is missing
                    }
                }

                for (int i = 0 ; i < imageBase64.length ; i++){
                    String shortenedString = imageBase64[i].substring(0, 20);
                    Log.d("base64", "image: " + shortenedString);
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "upload_docu.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    // Handle the response, if needed
                                    Log.d("Pasok", "pasok");
                                } catch (Throwable e) {
                                    Log.d("CatchError", String.valueOf(e));
                                    // Toast.makeText(Login.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        Log.d("image", "valid: " + imageBase64[0]);
                        Log.d("image", "mayor: " + imageBase64[1]);
                        Log.d("image", "bir: " + imageBase64[2]);
                        Log.d("image", "dti: " + imageBase64[3]);
                        Log.d("image", "sanitary: " + imageBase64[4]);

                        paramV.put("idStore", String.valueOf(id));
                        paramV.put("validId", imageBase64[0]);
                        paramV.put("permitMayor", imageBase64[1]);
                        paramV.put("permitBir", imageBase64[2]);
                        paramV.put("permitDti", imageBase64[3]);
                        paramV.put("permitSanitary", imageBase64[4]);

                        return paramV;
                    }
                };
                queue.add(stringRequest);

                // Rest of the code (if needed)

                // Redirect to the DashboardFragment after saving edits
//                Bundle bundle = new Bundle();
//                DashboardFragment fragment = new DashboardFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
            }
        });


        return root;
    }

    public void readDocuments(){
        RequestQueue requestQueueDocument = Singleton.getsInstance(getActivity()).getRequestQueue();
        JsonArrayRequest jsonArrayRequestDocuments = new JsonArrayRequest(Request.Method.GET, JSON_URL + "apidocuget.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ResponseItemLength", String.valueOf(response.length()));
                List<OrderItemModel> orderItemModels = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObjectDocument = response.getJSONObject(i);
                        if (jsonObjectDocument.getInt("idStore") == id) {
                            String validId = jsonObjectDocument.getString("validId");
                            String permitMayor = jsonObjectDocument.getString("permitMayor");
                            String permitBir = jsonObjectDocument.getString("permitBir");
                            String permitDti = jsonObjectDocument.getString("permitDti");
                            String permitSanitary = jsonObjectDocument.getString("permitSanitary");

                            if(validId.length() != 0) {
                                byte[] byteArrayValidId = Base64.decode(validId, Base64.DEFAULT);
                                Bitmap bitmapValidId = BitmapFactory.decodeByteArray(byteArrayValidId, 0, byteArrayValidId.length);
                                iv_valid_id_placeholder.setImageBitmap(bitmapValidId);
                            } if(permitMayor.length() != 0){
                                byte[] byteArrayMayor = Base64.decode(permitMayor, Base64.DEFAULT);
                                Bitmap bitmapMayor = BitmapFactory.decodeByteArray(byteArrayMayor, 0, byteArrayMayor.length);
                                iv_mayors_permit_placeholder.setImageBitmap(bitmapMayor);
                            } if(permitBir.length() != 0){
                                byte[] byteArrayBir = Base64.decode(permitBir, Base64.DEFAULT);
                                Bitmap bitmapBir = BitmapFactory.decodeByteArray(byteArrayBir, 0, byteArrayBir.length);
                                iv_bir_permit_placeholder.setImageBitmap(bitmapBir);
                            } if(permitDti.length() != 0){
                                byte[] byteArrayDti = Base64.decode(permitDti, Base64.DEFAULT);
                                Bitmap bitmapDti = BitmapFactory.decodeByteArray(byteArrayDti, 0, byteArrayDti.length);
                                iv_dti_permit_placeholder.setImageBitmap(bitmapDti);
                            } if(permitSanitary.length() != 0){
                                byte[] byteArraySanitary = Base64.decode(permitSanitary, Base64.DEFAULT);
                                Bitmap bitmapSanitary = BitmapFactory.decodeByteArray(byteArraySanitary, 0, byteArraySanitary.length);
                                iv_sanitary_permit_placeholder.setImageBitmap(bitmapSanitary);
                            }
                        }
                    }
                    Log.d("ResponseOrderItemSize", String.valueOf(orderItemModels.size()));
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
        requestQueueDocument.add(jsonArrayRequestDocuments);
    }

//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getData();
//                Context context = getActivity();
//                path = RealPathUtil.getRealPath(context, uri);
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                iv_valid_id_placeholder.setImageBitmap(bitmap);
//            }
//        } catch (Exception e){
//            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
}