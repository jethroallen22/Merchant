package com.example.merchant.activities.ui.addproduct;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.merchant.R;
import com.example.merchant.activities.ui.Dashboard.DashboardViewModel;
import com.example.merchant.activities.ui.slideshow.ProductsFragment;
import com.example.merchant.databinding.FragmentAddProductBinding;
import com.example.merchant.databinding.FragmentDashboardBinding;
import com.example.merchant.models.ProductModel;

import org.w3c.dom.Text;

public class AddProductFragment extends Fragment {

    private FragmentAddProductBinding binding;
    EditText name_text_input, description_text_input, calories_text_input, price_text_input;
    Button btn_add_product, btn_upload;

    private String product_name, description, calories_tmp, price_tmp;
    int calories;
    float price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        calories_text_input = root.findViewById(R.id.calories_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_add_product = root.findViewById(R.id.btn_add_product);
        btn_upload = root.findViewById(R.id.btn_upload);

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_name = String.valueOf(name_text_input.getText());
                description = String.valueOf(name_text_input.getText());
                calories = Integer.parseInt((calories_text_input.getText().toString()));
                price = Float.parseFloat((price_text_input.getText().toString()));


                ProductModel productModel = new ProductModel();
                productModel.setProductName(product_name);
                productModel.setProductDescription(description);
//                productModel.setProduct_calories(calories);
//                productModel.setProduct_price(price);

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