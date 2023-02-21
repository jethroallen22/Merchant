package com.example.merchant.activities.ui.editproduct;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.merchant.R;
import com.example.merchant.activities.ui.Dashboard.DashboardViewModel;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.databinding.FragmentDashboardBinding;
import com.example.merchant.databinding.FragmentEditProductBinding;
import com.example.merchant.models.OrderModel;
import com.example.merchant.models.ProductModel;

public class EditProductFragment extends Fragment {

    private FragmentEditProductBinding binding;

    EditText name_text_input, description_text_input, preptime_text_input,category_text_input, servesize_text_input, price_text_input;
    Button btn_edit_product, btn_upload;

    private String product_name, description, category, servesize, prep_time_tmp, price_tmp;
    private int prep_time = 0;
    private float price = 0;
    private ProductModel productModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        preptime_text_input = root.findViewById(R.id.preptime_text_input);
        category_text_input = root.findViewById(R.id.category_text_input);
        servesize_text_input = root.findViewById(R.id.servesize_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_edit_product = root.findViewById(R.id.btn_edit_product);
        btn_upload = root.findViewById(R.id.btn_upload);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productModel = new ProductModel();
            productModel = bundle.getParcelable("Product");
        }

        name_text_input.setText(productModel.getProductName());
        description_text_input.setText(productModel.getProductDescription());
        preptime_text_input.setText(String.valueOf(productModel.getProductPrepTime()));
        category_text_input.setText(productModel.getProductTag());
        servesize_text_input.setText(productModel.getProductServingSize());
        price_text_input.setText((int) productModel.getProductPrice());

        btn_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_name = String.valueOf(name_text_input.getText());
                description = String.valueOf(name_text_input.getText());
                category = String.valueOf(category_text_input.getText());
                servesize = String.valueOf(servesize_text_input.getText());
                prep_time_tmp = String.valueOf(preptime_text_input.getText());
                price_tmp = String.valueOf(price_text_input.getText());

                /////////////////// UPDATE DB//////////////////

                ProductModel productModel = new ProductModel();

                Bundle bundle = new Bundle();
                ProductsFragment productsFragment = new ProductsFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,productsFragment).commit();
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