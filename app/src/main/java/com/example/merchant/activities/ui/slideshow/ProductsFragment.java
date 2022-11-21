package com.example.merchant.activities.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merchant.R;
import com.example.merchant.activities.ui.addproduct.AddProductFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.adapters.ProductAdapter;
import com.example.merchant.databinding.FragmentProductsBinding;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;
    //Product List Recycler View
    RecyclerView rv_products;
    List<ProductModel> product_list;
    ProductAdapter productAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_products = root.findViewById(R.id.rv_products);
        product_list = new ArrayList<>();
        product_list.add(new ProductModel("test", "Burger Mcdo", "Tasty delicious burger Mcdo", "Mcdo - Binondo", 45F, 350));
        product_list.add(new ProductModel("test", "Chicken Ala king", "Tasty delicious burger Mcdo", "Mcdo - Binondo", 45F, 350));

        productAdapter = new ProductAdapter(getActivity(), product_list);
        rv_products.setAdapter(productAdapter);
        rv_products.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rv_products.setHasFixedSize(true);
        rv_products.setNestedScrollingEnabled(false);


        binding.fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AddProductFragment fragment = new AddProductFragment();
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
}