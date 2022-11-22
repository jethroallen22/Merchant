package com.example.merchant.activities.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
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
import com.example.merchant.activities.ui.editproduct.EditProductFragment;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.adapters.ProductAdapter;
import com.example.merchant.databinding.FragmentProductsBinding;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment implements RecyclerViewInterface, ProductAdapter.OnItemClickListener {

    private FragmentProductsBinding binding;
    //Product List Recycler View
    RecyclerView rv_products;
    List<ProductModel> product_list;
    ProductAdapter productAdapter;
    RecyclerViewInterface recyclerViewInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductsViewModel productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rv_products = root.findViewById(R.id.rv_products);
        product_list = new ArrayList<>();
//        product_list.add(new ProductModel("test", "Burger Mcdo", "Tasty delicious burger Mcdo", "Mcdo - Binondo", 45F, 350));
//        product_list.add(new ProductModel("test", "Chicken Ala king", "Tasty delicious burger Mcdo", "Mcdo - Binondo", 45F, 350));
        productAdapter = new ProductAdapter(getActivity(), product_list, this);
        rv_products.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                product_list.remove(position);
                productAdapter.notifyItemRemoved(position);
            }
        });

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

    //Function
    //Display Product BottomSheet

    public void showBottomSheet(int position){
        String TAG = "Bottomsheet";
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        Log.d(TAG, "final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);");
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(
                        R.layout.product_bottom_sheet_layout,
                        getActivity().findViewById(R.id.product_bottomSheet_container)
                );
        Log.d(TAG,"bottomSheetView = LayoutInflater.from");
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public void onItemClick(int position) {
        showBottomSheet(position);
    }

    @Override
    public void onItemClickEdit(int position){
        Log.d("Test", "Success");
        EditProductFragment fragment = new EditProductFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,fragment).commit();
    }
}