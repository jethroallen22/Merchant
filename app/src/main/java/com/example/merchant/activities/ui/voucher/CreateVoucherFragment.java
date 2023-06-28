package com.example.merchant.activities.ui.voucher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.Home;
import com.example.merchant.databinding.FragmentCreateVoucherBinding;
import com.example.merchant.models.IPModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateVoucherFragment extends Fragment {

    private FragmentCreateVoucherBinding binding;

    private static String JSON_URL;
    private IPModel ipModel;
    EditText et_vouchername, et_voucheramount, et_voucherminimum;
    DatePicker startDatePickerVoucher, endDatePickerVoucher;
    TextView tv_voucherstatus;
    Button btn_createvoucher;
    public static String name = "";
    public static String email = "";
    public static int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateVoucherBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
//            Log.d("Merchant Special Stat", name + " " + id + " " + email);
        }

        et_vouchername = root.findViewById(R.id.et_vouchername);
        et_voucheramount = root.findViewById(R.id.et_voucheramount);
        et_voucherminimum = root.findViewById(R.id.et_voucherminimum);
        startDatePickerVoucher = root.findViewById(R.id.startDatePickerVoucher);
        endDatePickerVoucher = root.findViewById(R.id.endDatePickerVoucher);
        tv_voucherstatus = root.findViewById(R.id.tv_voucherstatus);
        btn_createvoucher = root.findViewById(R.id.btn_createvoucher);

        btn_createvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVoucher();
            }
        });

        return root;
    }

    private void createVoucher() {
        String vouchername = et_vouchername.getText().toString();
        String voucheramountStr = et_voucheramount.getText().toString();
        String voucherminStr = et_voucherminimum.getText().toString();
        if (vouchername.isEmpty() || voucheramountStr.isEmpty() || voucherminStr.isEmpty()) {
            tv_voucherstatus.setText("All inputs should be filled!");
            return;
        }
        int voucheramount = Integer.parseInt(voucheramountStr);
        int vouchermin = Integer.parseInt(voucherminStr);
        int startday = startDatePickerVoucher.getDayOfMonth();
        int startmonth = startDatePickerVoucher.getMonth();
        int startyear = startDatePickerVoucher.getYear();
        Calendar startcalendar = Calendar.getInstance();
        startcalendar.set(startyear, startmonth, startday);

        int endday = endDatePickerVoucher.getDayOfMonth();
        int endmonth = endDatePickerVoucher.getMonth();
        int endyear = endDatePickerVoucher.getYear();
        Calendar endcalendar = Calendar.getInstance();
        endcalendar.set(endyear, endmonth, endday);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatedStartDate = sdf.format(startcalendar.getTime());
        Date startDate;
        try {
            startDate = sdf.parse(formatedStartDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.d("startDate", formatedStartDate);

        String formatedEndDate = sdf.format(endcalendar.getTime());
        Date endDate;
        try {
            endDate = sdf.parse(formatedEndDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Log.d("endDate", formatedEndDate);

        try {
            if (startDate.before(endDate) && !vouchername.isEmpty() && voucheramount != 0 && vouchermin !=0) {
                sendVoucherToDb(vouchername, voucheramount, vouchermin, formatedStartDate, formatedEndDate);

                Intent intent = new Intent(getActivity(), Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (startDate.after(endDate)){
                tv_voucherstatus.setText("Voucher Start Date should be before the End Date. Please try again!");
            } else if (vouchername.isEmpty()){
                tv_voucherstatus.setText("Please Input Voucher Name!");
            } else if (voucheramount == 0){
                tv_voucherstatus.setText("Please Input Voucher Amount!");
            }else if (vouchermin == 0){
                tv_voucherstatus.setText("Please Input Voucher Minimum!");
            } else {
                tv_voucherstatus.setText("All inputs should be filled!");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendVoucherToDb(String vouchername, int voucheramount, int vouchermin, String formatedStartDate, String formatedEndDate) throws ParseException {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "voucher.php",
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
                params.put("voucherName", vouchername);
                params.put("storeId", String.valueOf(id));
                params.put("voucherAmount", String.valueOf(voucheramount));
                params.put("voucherMin", String.valueOf(vouchermin));
                params.put("startDate", formatedStartDate);
                params.put("endDate", formatedEndDate);
                params.put("status", "pending");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}
