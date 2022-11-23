package com.example.merchant.activities.ui.addproduct;

import androidx.lifecycle.ViewModelProvider;

import android.media.Image;
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
import android.widget.ImageView;
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
    EditText name_text_input, description_text_input, preptime_text_input,category_text_input, servesize_text_input, price_text_input;
    Button btn_add_product, btn_upload;

    private String product_name, description, category, servesize, prep_time_tmp, price_tmp;
    int prep_time = 0;
    float price = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);

        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Initialize
        name_text_input = root.findViewById(R.id.name_text_input);
        description_text_input = root.findViewById(R.id.description_text_input);
        preptime_text_input = root.findViewById(R.id.preptime_text_input);
        category_text_input = root.findViewById(R.id.category_text_input);
        servesize_text_input = root.findViewById(R.id.servesize_text_input);
        price_text_input = root.findViewById(R.id.price_text_input);
        btn_add_product = root.findViewById(R.id.btn_add_product);
        btn_upload = root.findViewById(R.id.btn_upload);

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_name = String.valueOf(name_text_input.getText());
                description = String.valueOf(name_text_input.getText());
                category = String.valueOf(category_text_input.getText());
                servesize = String.valueOf(servesize_text_input.getText());
                prep_time_tmp = String.valueOf(preptime_text_input.getText());
                price_tmp = String.valueOf(price_text_input.getText());
//                if (preptime_text_input.getText().toString() != null)
//                    prep_time = Integer.parseInt(String.valueOf((preptime_text_input.getText())));
//                else
//                    prep_time = 0;
//                if (price_text_input.getText().toString() != null)
//                    price = Float.parseFloat((price_text_input.getText().toString()));
//                else
//                    price = 0F;

                if (product_name.isEmpty() || description.isEmpty() || category.isEmpty() || servesize.isEmpty()
                        || prep_time_tmp.isEmpty() || price_tmp.isEmpty()){
                    if (product_name.isEmpty())
                        name_text_input.setError("Please insert Product Name!");
                    if (description.isEmpty())
                        description_text_input.setError("Please insert Description!");
                    if (category.isEmpty())
                        category_text_input.setError("Please insert Category!");
                    if (servesize.isEmpty())
                        servesize_text_input.setError("Please insert Serving Size!");
                    if (prep_time_tmp.isEmpty())
                        preptime_text_input.setError("Please insert Preparation Time!");
                    if (price_tmp.isEmpty())
                        price_text_input.setError("Please insert Price!");
                } else {
                    ProductModel productModel = new ProductModel();
                    prep_time = Integer.parseInt(prep_time_tmp);
                    price = Float.parseFloat(price_tmp);

                    productModel.setProductImage("");
                    productModel.setStore_idStore(1);
                    productModel.setProductName(product_name);
                    productModel.setProductDescription(description);
                    productModel.setProductPrepTime(prep_time);
                    productModel.setProductTag(category);
                    productModel.setProductServingSize(servesize);
                    productModel.setProductPrice(price);

                    Bundle bundle = new Bundle();
                    ProductsFragment productsFragment = new ProductsFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,productsFragment).commit();
                }


//                ProductModel productModel = new ProductModel();
//                productModel.setProductImage("");
//                productModel.setStore_idStore(1);
//                productModel.setProductName(product_name);
//                productModel.setProductDescription(description);
//                productModel.setProductPrepTime(prep_time);
//                productModel.setProductTag(category);
//                productModel.setProductServingSize(servesize);
//                productModel.setProductPrice(price);
//
//                Bundle bundle = new Bundle();
//                ProductsFragment productsFragment = new ProductsFragment();
//
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home,productsFragment).commit();


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