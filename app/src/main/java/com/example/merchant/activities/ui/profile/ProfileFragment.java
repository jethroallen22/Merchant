package com.example.merchant.activities.ui.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.RealPathUtil;
import com.example.merchant.activities.ui.Dashboard.DashboardFragment;
import com.example.merchant.databinding.FragmentProfileBinding;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.StoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    ImageView iv_image_placeholder, iv_edit_name, iv_edit_description, iv_edit_open, iv_edit_close, iv_save_name, iv_save_description, iv_save_open, iv_save_close;
    CardView cv_edit_picture;
    EditText tv_edit_name, tv_edit_description, tv_edit_open, tv_edit_close;
    Button btn_save_edit;
    String storeImage, storeName, storeDescription, storeLocation, storeCategory;
    Float storeRating, storePopularity;
    int idStore, storeStartTime, storeEndTime;
    StoreModel storeModel;
    List<StoreModel> storeModelList;
    Bitmap bitmap;
    String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        storeModelList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            storeModelList = (List<StoreModel>) bundle.getSerializable("stores");
            Log.d("LISTSIZE", String.valueOf(storeModelList.size()));
            idStore = bundle.getInt("id");
            for (int i = 0 ; i < storeModelList.size() ; i++){
                if(idStore == storeModelList.get(i).getStore_id()){
                    storeName = storeModelList.get(i).getStore_name();
                    storeDescription = storeModelList.get(i).getStore_description();
                    storeLocation = storeModelList.get(i).getStore_location();
                    storeCategory = storeModelList.get(i).getStore_category();
                    storeRating = storeModelList.get(i).getStore_rating();
                    storePopularity = storeModelList.get(i).getStore_popularity();
                    bitmap = storeModelList.get(i).getBitmapImage();
                    storeStartTime = storeModelList.get(i).getStore_open();
                    storeEndTime = storeModelList.get(i).getStore_closing();
                    Log.d("TESTNAME: ", storeModelList.get(i).getStore_name());
                }
            }

        }

        //Initialize views
        iv_image_placeholder = root.findViewById(R.id.iv_profile_placeholder);
        cv_edit_picture = root.findViewById(R.id.cv_edit_picture);
        tv_edit_name = root.findViewById(R.id.tv_profile_name);
        tv_edit_description = root.findViewById(R.id.tv_profile_description);
        tv_edit_open = root.findViewById(R.id.tv_profile_open);
        tv_edit_close = root.findViewById(R.id.tv_profile_close);
        btn_save_edit = root.findViewById(R.id.btn_save_edit);
        iv_edit_name = root.findViewById(R.id.iv_edit_name);
        iv_edit_description = root.findViewById(R.id.iv_edit_description);
        iv_edit_open = root.findViewById(R.id.iv_edit_open);
        iv_edit_close = root.findViewById(R.id.iv_edit_close);
        iv_save_name = root.findViewById(R.id.iv_save_name);
        iv_save_description = root.findViewById(R.id.iv_save_description);
        iv_save_open = root.findViewById(R.id.iv_save_open);
        iv_save_close = root.findViewById(R.id.iv_save_close);

        //Disable edit texts
        tv_edit_name.setEnabled(false);
        tv_edit_description.setEnabled(false);
        tv_edit_open.setEnabled(false);
        tv_edit_close.setEnabled(false);

        //Set default profile image
        iv_image_placeholder.setImageBitmap(bitmap);

        //Set edit text hints
        tv_edit_name.setHint(storeName);
        tv_edit_description.setHint(storeDescription);
        tv_edit_open.setHint(String.valueOf(storeStartTime));
        tv_edit_close.setHint(String.valueOf(storeEndTime));

        iv_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_name.setEnabled(true);
            }
        });

        iv_edit_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_description.setEnabled(true);
            }
        });

        iv_edit_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_open.setEnabled(true);
            }
        });

        iv_edit_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_close.setEnabled(true);
            }
        });

        iv_save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit_name.setEnabled(false);
                storeName = tv_edit_name.getText().toString().trim();
                Log.d("EDIT", storeName);
            }
        });

        iv_save_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_save_description.setEnabled(false);
                storeDescription = tv_edit_description.toString().trim();
            }
        });

        iv_save_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_save_open.setEnabled(false);
                storeStartTime = Integer.parseInt(tv_edit_open.toString().trim());
            }
        });

        iv_save_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_save_close.setEnabled(false);
                storeEndTime = Integer.parseInt(tv_edit_close.toString().trim());
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
                        iv_image_placeholder.setImageBitmap(bitmap);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        cv_edit_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });

        btn_save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    final String name = storeName;
                    final String description = storeDescription;
                    final int open = storeStartTime;
                    final int close = storeEndTime;

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL+ "update_store.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
//                                            Intent intent = new Intent(getContext(), Home.class);
//                                            intent.putExtra("name",name2);
//                                            intent.putExtra("id",id);
//                                            intent.putExtra("image", base64Image);
//                                            intent.putExtra("email", email2);
//                                            intent.putExtra("contact", contact2);
//                                            intent.putExtra("password", password2);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(intent);
//                                        }
                                    }
                                    catch (Throwable e) {
                                        Log.d("Catch", String.valueOf(e));
                                        //Toast.makeText(Login.this, "Invalid Email and/or Password", Toast.LENGTH_SHORT).show();
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
                            paramV.put("idStore", String.valueOf(idStore));
                            paramV.put("storeImage", base64Image);
                            paramV.put("storeName", name);
                            paramV.put("storeDescription", description);
                            paramV.put("storeStartTime", String.valueOf(open));
                            paramV.put("storeEndTime", String.valueOf(close));
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);

                } else
                    Toast.makeText(getActivity().getApplicationContext(),"Please select an image first!", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                DashboardFragment fragment = new DashboardFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                Context context = getActivity();
                path = RealPathUtil.getRealPath(context, uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                iv_image_placeholder.setImageBitmap(bitmap);
                Log.d("IMAGE", String.valueOf(iv_image_placeholder.getDrawable()));
            }
        } catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}