package com.example.merchant.activities.ui.feedback;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchant.R;
import com.example.merchant.activities.ui.checkout.Checkout2Fragment;
import com.example.merchant.activities.ui.slideshow.ProductsViewModel;
import com.example.merchant.adapters.OrderItemsAdapter;
import com.example.merchant.databinding.FragmentFeedbackSummaryBinding;
import com.example.merchant.databinding.FragmentOrderSummaryBinding;
import com.example.merchant.models.IPModel;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackSummaryFragment extends Fragment {

    private FragmentFeedbackSummaryBinding binding;
    //Cart List Recycler View
    RecyclerView rv_order_items;
    OrderModel order;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;
    TextView tv_order_id, tv_name, tv_total_price, tv_feedback;
    Button btn_refund, btn_reject, btn_send;
    ImageView iv_proof;
    Bitmap bitmap;
    EditText et_feedback;
    public static String name = "";
    public static String email = "";
    public static int id;
    //School IP
    private static String JSON_URL;
    private IPModel ipModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        ipModel = new IPModel();
        JSON_URL = ipModel.getURL();

        binding = FragmentFeedbackSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_order_items = root.findViewById(R.id.rv_order_items);
        tv_order_id = root.findViewById(R.id.tv_order_id);
        tv_name = root.findViewById(R.id.tv_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        tv_feedback = root.findViewById(R.id.tv_feedback);
        btn_reject = root.findViewById(R.id.btn_reject);
        btn_refund = root.findViewById(R.id.btn_refund);
        iv_proof = root.findViewById(R.id.iv_proof);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            order = new OrderModel();
            order = bundle.getParcelable("Order");
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            Log.d("Bundle orderStat", order.getOrderStatus());
            bitmap = order.getBitmapImage();
        }
        tv_order_id.setText("Order ID: " + String.valueOf(order.getIdOrder()));
        tv_name.setText(String.valueOf(order.getUsers_name()));
        tv_total_price.setText(String.valueOf(order.getOrderItemTotalPrice()));
        tv_feedback.setText(order.getFeedback());

        iv_proof.setImageBitmap(bitmap);

        order_item_list = new ArrayList<>();

        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list());
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);


        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus(order.getIdOrder(), "refund");
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                FeedbackFragment fragment = new FeedbackFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void UpdateStatus(int idOrder, String status){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL + "feedback.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", String.valueOf(error));
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idorder", String.valueOf(order.getIdOrder()));
                params.put("status", status);
                params.put("iduser", String.valueOf(order.getIdUser()));
                params.put("title", "Order ID: " + order.getIdOrder());
                params.put("type", "orderprocess");
                if (status.equalsIgnoreCase("refund")){
                    params.put("description", "Your feedback was seen and your order has been refunded");
                } else if (status.equalsIgnoreCase("reject")){
                    params.put("description", String.valueOf(et_feedback.getText()));
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
    public void showBottomSheet(){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.feedback_bottom_sheet_layout,
                        getActivity().findViewById(R.id.feedback_bottomSheet_container)
                );
        et_feedback = bottomSheetView.findViewById(R.id.et_feedback);
        btn_send = bottomSheetView.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus(order.getIdOrder(), "reject");
                bottomSheetDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                FeedbackFragment fragment = new FeedbackFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}