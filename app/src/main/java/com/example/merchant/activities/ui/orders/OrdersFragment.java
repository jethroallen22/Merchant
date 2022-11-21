package com.example.merchant.activities.ui.orders;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.merchant.R;
import com.example.merchant.activities.ui.ordersummary.OrderSummaryFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.databinding.FragmentOrdersBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment implements RecyclerViewInterface {

    private FragmentOrdersBinding binding;
    //Cart List Recycler View
    RecyclerView rv_orders;
    List<OrderModel> order_list;
    OrderAdapter orderAdapter;
    List<OrderItemModel> order_item_list;
//    RecyclerViewInterface recyclerViewInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrdersViewModel ordersViewModel =
                new ViewModelProvider(this).get(OrdersViewModel.class);

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        order_item_list = new ArrayList<>();
        order_item_list.add(new OrderItemModel("Burger Mcdo", 2, 80F));
        order_item_list.add(new OrderItemModel("Chicken Ala King",1,89F));
        order_item_list.add(new OrderItemModel("BFF Fries",2,79F));

        rv_orders = root.findViewById(R.id.rv_orders);
        order_list = new ArrayList<>();
        order_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now().getHour()), "3.5", order_item_list, order_item_list.size(), 123F));
        order_list.add(new OrderModel("Popoy Batumbakal", "General Nakar, Quezon", String.valueOf(LocalDateTime.now().getHour()), "43.5", order_item_list, order_item_list.size(), 123F));

        orderAdapter = new OrderAdapter(getActivity(), order_list, this);
        rv_orders.setAdapter(orderAdapter);
        rv_orders.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_orders.setHasFixedSize(true);
        rv_orders.setNestedScrollingEnabled(false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {

//        Log.d("TAG", "Success");
//        Bundle bundle = new Bundle();
//        //bundle.putInt("StoreImage", home_pop_store_list.get(position).getStore_image());
//        bundle.putString("StoreName", home_pop_store_list.get(position).getStore_name());
//        bundle.putString("StoreAddress", "Esterling Heights Subdivision, Guintorilan City");
//        bundle.putString("StoreCategory", home_pop_store_list.get(position).getStore_category());

        Bundle order = new Bundle();
        //bundle.putParcelableArrayList(order_list);
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        //bundle.putSerializable("OrderSummary", order_list);
        //order.putParcelableArrayList("OrderItemList", (ArrayList<? extends Parcelable>) order_list); //
        order.putParcelable("Order",order_list.get(position));
        fragment.setArguments(order);
        Log.d("TAG", "Success");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
        Log.d("TAG", "Success Click");
    }
}