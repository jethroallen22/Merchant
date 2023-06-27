package com.example.merchant.activities.ui.deals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.Home;
import com.example.merchant.adapters.DealsAdapter;
import com.example.merchant.databinding.FragmentDealsCreateBinding;
import com.example.merchant.models.IPModel;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DealsCreateFragment extends Fragment {
    private FragmentDealsCreateBinding binding;

    private static String JSON_URL;
    private IPModel ipModel;

    EditText et_title, et_percentage;
    DatePicker dt_startdate, dt_enddate;

    TextView tv_specialtagstatus;
    Button btn_createdeals;
    public static String name = "";
    public static String email = "";
    public static int id;
    List<Integer> checkedList;
    List<String> checkedProducts;
    ListView lv_deals;
    ArrayAdapter<String> listAdapter;
    RecyclerView rv_deals;
    DealsAdapter dealsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDealsCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            checkedList = bundle.getIntegerArrayList("checkedList");
            checkedProducts = bundle.getStringArrayList("checkedProducts");
//            Log.d("Merchant", name + " " + id + " " + email);
        }

        et_title = root.findViewById(R.id.et_title);
        et_percentage = root.findViewById(R.id.et_percentage);
        dt_startdate = root.findViewById(R.id.startDatePicker);
        dt_enddate = root.findViewById(R.id.endDatePicker);
        btn_createdeals = root.findViewById(R.id.btn_createdeals);
        rv_deals = root.findViewById(R.id.rv_deals);
        rv_deals.setLayoutManager(new LinearLayoutManager(getContext()));
        dealsAdapter = new DealsAdapter(getContext(), checkedProducts);
        rv_deals.setAdapter(dealsAdapter);

//        lv_deals = root.findViewById(R.id.lv_deals);
//        Log.d("deal", "Size: " + checkedProducts.size());
//
//        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, checkedProducts);
////        Log.d("DC size", String.valueOf(checkedProducts.size()));
////        for (String deal: checkedProducts) {
////            Log.d("DC", deal);
////            listAdapter.add(deal);
////        }
//        lv_deals.setAdapter(listAdapter);


        btn_createdeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTag();
            }
        });
        return root;
    }

    public void createTag(){
        String title = et_title.getText().toString();
        String percentage = et_percentage.getText().toString();
        int startday = dt_startdate.getDayOfMonth();
        int startmonth = dt_startdate.getMonth();
        int startyear = dt_startdate.getYear();
        Calendar startcalendar = Calendar.getInstance();
        startcalendar.set(startyear, startmonth, startday);

        int endday = dt_enddate.getDayOfMonth();
        int endmonth = dt_enddate.getMonth();
        int endyear = dt_enddate.getYear();
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.set(endyear, endmonth, endday);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatedStartDate = sdf.format(startcalendar.getTime());
        Date startdate;
        try {
            startdate = sdf.parse(formatedStartDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.d("startdate", formatedStartDate);

        String formatedEndDate = sdf.format(endcalendar.getTime());
        Date enddate;
        try {
           enddate = sdf.parse(formatedEndDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.d("enddate", formatedEndDate);

        try {
            if (startdate.before(enddate) && !title.isEmpty() && !percentage.isEmpty()) {
                sendDealstoDb(title,percentage,formatedStartDate,formatedEndDate);

                Intent intent = new Intent(getActivity(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (startdate.after(enddate)){
                tv_specialtagstatus.setText("Voucher Start Date should be before the End Date. Please try again!");
            } else if (title.isEmpty()){
                tv_specialtagstatus.setText("Please Input Title!");
            } else if (percentage.isEmpty()){
                tv_specialtagstatus.setText("Please Input Percentage!");
            } else {
                tv_specialtagstatus.setText("All inputs should be filled!");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendDealstoDb(String title, String percentage, String formatedStartDate, String formatedEndDate) throws ParseException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "addDeals.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        // Process the response here
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                        // Handle the error here
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("storeId", String.valueOf(id));
                Gson gson = new Gson();
                String jsonArray = gson.toJson(checkedList);
                params.put("data", jsonArray);
                params.put("title", title);
                params.put("percentage", percentage);
                params.put("startdate", formatedStartDate);
                params.put("enddate", formatedEndDate);
                params.put("status", "pending");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
