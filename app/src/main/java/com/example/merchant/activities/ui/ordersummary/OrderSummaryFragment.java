package com.example.merchant.activities.ui.ordersummary;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.merchant.R;
import com.example.merchant.activities.ui.slideshow.ProductsViewModel;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.adapters.OrderItemsAdapter;
import com.example.merchant.databinding.FragmentOrderSummaryBinding;
import com.example.merchant.databinding.FragmentProductsBinding;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSummaryFragment extends Fragment {

    private FragmentOrderSummaryBinding binding;
    //Cart List Recycler View
    RecyclerView rv_order_items;
    OrderModel order;
    List<OrderItemModel> order_item_list;
    OrderItemsAdapter orderItemsAdapter;
    TextView tv_order_id, tv_name, tv_total_price;
    Button btn_place_order, btn_cancel_order;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_order_items = root.findViewById(R.id.rv_order_items);
        tv_order_id = root.findViewById(R.id.tv_order_id);
        tv_name = root.findViewById(R.id.tv_name);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        btn_cancel_order = root.findViewById(R.id.btn_cancel_order);
        btn_place_order = root.findViewById(R.id.btn_place_order);

        Bundle bundle = this.getArguments();
        order = new OrderModel();
        order = bundle.getParcelable("Order");

        tv_order_id.setText(String.valueOf(order.getIdOrder()));
        tv_name.setText(String.valueOf(order.getUsers_id()));
        tv_total_price.setText(String.valueOf(order.getOrderItemTotalPrice()));
        //tv_address.setText(order.getAddress());
        //tv_distance.setText("Distance from you: " + order.getDistance() + "km");

        order_item_list = new ArrayList<>();
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));
//        order_item_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));

        orderItemsAdapter = new OrderItemsAdapter(getActivity(), order.getOrderItem_list());
        rv_order_items.setAdapter(orderItemsAdapter);
        rv_order_items.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_order_items.setHasFixedSize(true);
        rv_order_items.setNestedScrollingEnabled(false);

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}