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
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.merchant.activities.MainActivity;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.databinding.FragmentAddProductBinding;
import com.example.merchant.models.IPModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductFragment extends Fragment {

    private FragmentAddProductBinding binding;
    EditText name_text_input, description_text_input, preptime_text_input,category_text_input, servesize_text_input, price_text_input;
    TextView tv_categ_status;
    Button btn_add_product, btn_upload;
    ImageView iv_product_img;
    private static String JSON_URL;
    private IPModel ipModel;

    private String product_name, description, category, servesize, prep_time, prep_time_tmp, price_tmp, weather_tmp;
    float price = 0;

    private CheckBox checkBoxHot, checkBoxCold, checkBoxAmerican, checkBoxChinese, checkBoxFilipino, checkBoxJapanese, checkBoxThai,
            checkBoxBreakfast, checkBoxLunch, checkBoxDessert, checkBoxPork, checkBoxBeef, checkBoxFish, checkBoxChicken,
            checkBoxSeafood, checkBoxNoodles, checkBoxPasta, checkBoxRamen, checkBoxBeverages, checkBoxJuice, checkBoxSoda,
            checkBoxMilkTea, checkBoxPizza, checkBoxSalad, checkBoxFastFood, checkBoxCrispy, checkBoxSpicy, checkBoxSweet,
            checkBoxBread, checkBoxQuick, checkBoxEasy, checkBoxHeavy, checkBoxBaked, checkBoxCoffee, checkBoxItalian, checkBoxIndian,
            checkBoxFrench, checkBoxMexican, checkBoxKorean, checkBoxMediterranean, checkBoxMainCourse, checkBoxAppetizer, checkBoxSoup,
            checkBoxSushi, checkBoxFruit;

    List<String> checkboxes_list;

    List<String> category_list;
//    private int c;
    String path;
    Bitmap bitmap;

    Spinner spinner;

    public static String name = "";
    public static String email = "";
    public static int id;

    ChipGroup cg_product;
    int maxSelectableChips = 1;
     String categorySelected;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
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

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        preptime_text_input = root.findViewById(R.id.preptime_text_input);
        tv_categ_status = root.findViewById(R.id.tv_categ_status);
        servesize_text_input = root.findViewById(R.id.servesize_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_add_product = root.findViewById(R.id.btn_add_product);
        btn_upload = root.findViewById(R.id.btn_upload);
        iv_product_img = root.findViewById(R.id.iv_product_img);
        spinner = root.findViewById(R.id.weather_spinner);
        checkBoxHot = root.findViewById(R.id.checkBoxHot);
        checkBoxCold = root.findViewById(R.id.checkBoxCold);
        checkBoxAmerican = root.findViewById(R.id.checkBoxAmerican);
        checkBoxChinese = root.findViewById(R.id.checkBoxChinese);
        checkBoxFilipino = root.findViewById(R.id.checkBoxFilipino);
        checkBoxJapanese = root.findViewById(R.id.checkBoxJapanese);
        checkBoxThai = root.findViewById(R.id.checkBoxThai);
        checkBoxBreakfast = root.findViewById(R.id.checkBoxBreakfast);
        checkBoxLunch = root.findViewById(R.id.checkBoxLunch);
        checkBoxDessert = root.findViewById(R.id.checkBoxDessert);
        checkBoxPork = root.findViewById(R.id.checkBoxPork);
        checkBoxChicken= root.findViewById(R.id.checkBoxChicken);
        checkBoxBeef = root.findViewById(R.id.checkBoxBeef);
        checkBoxFish = root.findViewById(R.id.checkBoxFish);
        checkBoxSeafood = root.findViewById(R.id.checkBoxSeafood);
        checkBoxNoodles = root.findViewById(R.id.checkBoxNoodles);
        checkBoxPasta = root.findViewById(R.id.checkBoxPasta);
        checkBoxRamen = root.findViewById(R.id.checkBoxRamen);
        checkBoxBeverages = root.findViewById(R.id.checkBoxBeverages);
        checkBoxJuice = root.findViewById(R.id.checkBoxJuice);
        checkBoxSoda = root.findViewById(R.id.checkBoxSoda);
        checkBoxMilkTea = root.findViewById(R.id.checkBoxMT);
        checkBoxPizza = root.findViewById(R.id.checkBoxPizza);
        checkBoxSalad = root.findViewById(R.id.checkBoxSalad);
        checkBoxFastFood = root.findViewById(R.id.checkBoxFastFood);
        checkBoxCrispy = root.findViewById(R.id.checkBoxCrispy);
        checkBoxSpicy = root.findViewById(R.id.checkBoxSpicy);
        checkBoxSweet = root.findViewById(R.id.checkBoxSweet);
        checkBoxBread = root.findViewById(R.id.checkBoxBread);
        checkBoxQuick = root.findViewById(R.id.checkBoxQuick);
        checkBoxEasy = root.findViewById(R.id.checkBoxEasy);
        checkBoxHeavy = root.findViewById(R.id.checkBoxHeavy);
        checkBoxBaked = root.findViewById(R.id.checkBoxBaked);
        checkBoxCoffee = root.findViewById(R.id.checkBoxCoffee);
        checkBoxItalian = root.findViewById(R.id.checkBoxItalian);
        checkBoxIndian = root.findViewById(R.id.checkBoxIndian);
        checkBoxFrench = root.findViewById(R.id.checkBoxFrench);
        checkBoxMexican = root.findViewById(R.id.checkBoxMexican);
        checkBoxKorean = root.findViewById(R.id.checkBoxKorean);
        checkBoxMediterranean = root.findViewById(R.id.checkBoxMediterranean);
        checkBoxMainCourse = root.findViewById(R.id.checkBoxMainCourse);
        checkBoxAppetizer = root.findViewById(R.id.checkBoxAppetizer);
        checkBoxSoup = root.findViewById(R.id.checkBoxSoup);
        checkBoxSushi = root.findViewById(R.id.checkBoxSushi);
        checkBoxFruit = root.findViewById(R.id.checkBoxFruit);
        cg_product=root.findViewById(R.id.cg_product);

        checkboxes_list = new ArrayList<>();

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

        checkBoxHot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkBoxCold.setChecked(false);
                    checkboxes_list.add("Hot");
                } else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Hot")){
                            checkboxes_list.remove(i);
                            Log.d("Check Hot", "Removed Hot from list");
                        }
                    }
                }
            }
        });

        checkBoxCold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkBoxHot.setChecked(false);
                    checkboxes_list.add("Cold");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Cold")){
                            checkboxes_list.remove(i);
                            Log.d("Check Cold", "Removed Cold from list");
                        }
                    }
                }
            }
        });

        checkBoxAmerican.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxAmerican.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("American");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("American")){
                            checkboxes_list.remove(i);
                            Log.d("Check American", "Removed American from list");
                        }
                    }
                }
            }
        });

        checkBoxChinese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxChinese.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Chinese");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Chinese")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxFilipino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxFilipino.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Filipino");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Filipino")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxJapanese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxJapanese.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Japanese");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Japanese")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxThai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxThai.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Thai");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Thai")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxItalian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxItalian.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Italian");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Italian")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxIndian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxIndian.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Indian");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Indian")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxFrench.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxFrench.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("French");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("French")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxMexican.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxMexican.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Mexican");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Mexican")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxKorean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxKorean.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Korean");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Korean")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxMediterranean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (counterCheckBox() <= 2 && checkBoxMediterranean.isChecked()){
                    Log.d("Check Counter", String.valueOf(counterCheckBox()));
                    checkboxes_list.add("Mediterranean");
                }else {
                    Toast toast = Toast.makeText(getContext(),"This is toast", Toast.LENGTH_SHORT);
                    toast.setText("Up to 2 Cuisines Only");
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    compoundButton.setChecked(false);
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Mediterranean")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxBreakfast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if ((categorySelected.compareTo("Breakfast")==0 || categorySelected.compareTo("Lunch")==0 ||
                                categorySelected.compareTo("Dessert")==0)){
                            Log.d("Add Tag", categorySelected);
                            checkBoxBreakfast.setChecked(false);
                        } else {
                            checkBoxLunch.setChecked(false);
                            checkBoxDessert.setChecked(false);
                            checkboxes_list.add("Breakfast");
                        }
                    } else {
                        checkBoxBreakfast.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Breakfast")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxLunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if ((categorySelected.compareTo("Breakfast")==0 || categorySelected.compareTo("Lunch")==0 ||
                                categorySelected.compareTo("Dessert")==0)){
                            Log.d("Add Tag", categorySelected);
                            checkBoxLunch.setChecked(false);
                        } else {
                            checkBoxBreakfast.setChecked(false);
                            checkBoxDessert.setChecked(false);
                            checkboxes_list.add("Lunch");
                        }
                    } else {
                        checkBoxLunch.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Lunch")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxDessert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if ((categorySelected.compareTo("Breakfast")==0 || categorySelected.compareTo("Lunch")==0 ||
                                categorySelected.compareTo("Dessert")==0)){
                            Log.d("Add Tag", categorySelected);
                            checkBoxDessert.setChecked(false);
                        } else {
                            checkBoxBreakfast.setChecked(false);
                            checkBoxLunch.setChecked(false);
                            checkboxes_list.add("Dessert");
                        }
                    } else {
                        checkBoxDessert.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Dessert")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxMainCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Main Course");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Main Course")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxAppetizer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Appetizer");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Appetizer")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxPork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Pork")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxPork.setChecked(false);
                        } else {
                            checkboxes_list.add("Pork");
                        }
                    } else {
                        checkBoxPork.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Pork")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxBeef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Beef")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxBeef.setChecked(false);
                        } else {
                            checkboxes_list.add("Beef");
                        }
                    } else {
                        checkBoxBeef.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Beef")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxFish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Fish")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxFish.setChecked(false);
                        } else {
                            checkboxes_list.add("Fish");
                        }
                    } else {
                        checkBoxFish.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Fish")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxChicken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Chicken")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxChicken.setChecked(false);
                        } else {
                            checkboxes_list.add("Chicken");
                        }
                    } else {
                        checkBoxChicken.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Chicken")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxSeafood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Seafood")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxSeafood.setChecked(false);
                        } else {
                            checkboxes_list.add("Seafood");
                        }
                    } else {
                        checkBoxSeafood.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Seafood")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxNoodles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Noodles")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxNoodles.setChecked(false);
                        } else {
                            checkboxes_list.add("Noodles");
                        }
                    } else {
                        checkBoxNoodles.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Noodles")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxPasta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Pasta");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Pasta")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxRamen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Ramen");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Ramen")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxBeverages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    if (categorySelected != null){
                        if (categorySelected.compareTo("Beverages")==0){
                            Log.d("Add Tag", categorySelected);
                            checkBoxBeverages.setChecked(false);
                        } else {
                            checkboxes_list.add("Beverages");
                        }
                    } else {
                        checkBoxBeverages.setChecked(false);
                        Toast.makeText(getContext(), "Please Select a Main Category", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Beverages")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxJuice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Juice");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Juice")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxSoda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Soda");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Soda")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxMilkTea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Milk Tea");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Milk Tea")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxCoffee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Coffee");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Coffee")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxPizza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Pizza");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Pizza")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxSalad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Salad");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Salad")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxFastFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Fast Food");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Fast Food")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxCrispy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Crispy");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Crispy")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxSpicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Spicy");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Spicy")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });
        checkBoxSweet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Sweet");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Sweet")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxBread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Bread");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Bread")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxQuick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Quick bites");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Quick bites")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxEasy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Easy to eat");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Easy to eat")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxHeavy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Heavy Meal");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Heavy Meal")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxBaked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Baked");
                    for (int i=0;i<checkboxes_list.size();i++){
                        Log.d("CheckList", checkboxes_list.get(i));
                    }
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Baked")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxSoup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Soup");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Soup")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxSushi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Sushi");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Sushi")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        checkBoxFruit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    checkboxes_list.add("Fruit");
                }else {
                    for (int i=0;i<checkboxes_list.size();i++){
                        if (checkboxes_list.get(i).equals("Fruit")){
                            checkboxes_list.remove(i);
                        }
                    }
                }
            }
        });

        binding.btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });

        category_list = new ArrayList<>();
//        category_list.add("American");
//        category_list.add("Chinese");
//        category_list.add("Filipino");
//        category_list.add("Japanese");
//        category_list.add("Thai");
        category_list.add("Breakfast");
        category_list.add("Lunch");
        category_list.add("Dessert");
        category_list.add("Pork");
        category_list.add("Chicken");
        category_list.add("Beef");
        category_list.add("Fish");
        category_list.add("Seafood");
        category_list.add("Noodles");
        category_list.add("Beverages");


        for (String str : category_list) {
            final Chip chip = new Chip(getActivity());
            chip.setText(str);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(R.color.chipDefault);
            chip.setTextColor(getResources().getColor(R.color.black));

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < cg_product.getChildCount(); i++) {
                        Chip currentChip = (Chip) cg_product.getChildAt(i);
                        if (currentChip != chip) {
                            currentChip.setChipBackgroundColorResource(R.color.chipDefault);
                            currentChip.setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                    chip.setChipBackgroundColorResource(R.color.mosibusPrimary);
                    chip.setTextColor(getResources().getColor(android.R.color.white));
                    categorySelected = (String) chip.getText();
                    Log.d("chups", categorySelected);
                }
            });

            cg_product.addView(chip);
        }



        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null){

                    product_name = String.valueOf(name_text_input.getText());
                    description = String.valueOf(description_text_input.getText());
//                    category = String.valueOf(category_text_input.getText());
                    servesize = String.valueOf(servesize_text_input.getText());
                    prep_time_tmp = String.valueOf(preptime_text_input.getText());
                    price_tmp = String.valueOf((price_text_input.getText()));
                    weather_tmp = spinner.getSelectedItem().toString().toLowerCase();

                    if (product_name.isEmpty() || description.isEmpty() || categorySelected.isEmpty() || servesize.isEmpty()
                            || prep_time_tmp.isEmpty() || price_tmp.isEmpty()){
                        if (product_name.isEmpty())
                            name_text_input.setError("Please insert Product Name!");
                        if (description.isEmpty())
                            description_text_input.setError("Please insert Description!");
                        if (categorySelected.isEmpty())
                            tv_categ_status.setError("Please Choose a Category!");
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
                    final int idStore = id;
                    final String pname = product_name;
                    final String pdesc = description;
                    final String ptag = categorySelected;
                    final String pservesize = servesize;
                    final String ppreptime = prep_time_tmp;
                    final float pprice = (float) (Float.parseFloat(price_tmp) + (Float.parseFloat(price_tmp) * 0.05));
                    final String pweather = weather_tmp;


                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        RequestQueue queue2 = Volley.newRequestQueue(getActivity().getApplicationContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "add_prod.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String result) {
                                        Log.d("QueryResult ", result );
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
                                paramV.put("productServingSize", pservesize + " pax");
                                paramV.put("productPrice", String.valueOf(pprice));
                                paramV.put("productPrepTime", ppreptime);
                                paramV.put("weather", pweather);
                                paramV.put("status", "pending");
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(checkboxes_list);
                                paramV.put("data", jsonArray);
                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    Toast.makeText(getContext(), "Your Product is Being Processed", Toast.LENGTH_SHORT).show();
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

    public int counterCheckBox() {
        int countCheckBox = 0;

        if (checkBoxAmerican.isChecked()){
            countCheckBox++;
            Log.d("Check Count Am", String.valueOf(countCheckBox));
        }
        if (checkBoxChinese.isChecked()){
            countCheckBox++;
            Log.d("Check Count Chin", String.valueOf(countCheckBox));
        }
        if (checkBoxFilipino.isChecked()){
            countCheckBox++;
            Log.d("Check Count Fil", String.valueOf(countCheckBox));
        }
        if (checkBoxJapanese.isChecked()){
            countCheckBox++;
            Log.d("Check Count Jap", String.valueOf(countCheckBox));
        }
        if (checkBoxThai.isChecked()){
            countCheckBox++;
            Log.d("Check Count Thai", String.valueOf(countCheckBox));
        }
        if (checkBoxItalian.isChecked()){
            countCheckBox++;
            Log.d("Check Count Ital", String.valueOf(countCheckBox));
        }
        if (checkBoxIndian.isChecked()){
            countCheckBox++;
            Log.d("Check Count Indi", String.valueOf(countCheckBox));
        }
        if (checkBoxFrench.isChecked()){
            countCheckBox++;
            Log.d("Check Count French", String.valueOf(countCheckBox));
        }
        if (checkBoxMexican.isChecked()){
            countCheckBox++;
            Log.d("Check Count Mex", String.valueOf(countCheckBox));
        }
        if (checkBoxKorean.isChecked()){
            countCheckBox++;
            Log.d("Check Count Kor", String.valueOf(countCheckBox));
        }
        if (checkBoxMediterranean.isChecked()){
            countCheckBox++;
            Log.d("Check Count Med", String.valueOf(countCheckBox));
        }
//        if (categorySelected.compareTo("American")==0 || categorySelected.compareTo("Chinese")==0 ||
//                categorySelected.compareTo("Filipino")==0 || categorySelected.compareTo("Japanese")==0 ||
//                categorySelected.compareTo("Thai")==0){
//            countCheckBox++;
//            Log.d("Check Count Category", categorySelected);
//        }
        return countCheckBox;
    }

}