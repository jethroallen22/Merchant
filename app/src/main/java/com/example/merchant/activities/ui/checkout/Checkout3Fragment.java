package com.example.merchant.activities.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.merchant.R;
import com.example.merchant.databinding.FragmentCheckout3Binding;
import com.example.merchant.models.OrderModel;

public class Checkout3Fragment extends Fragment{

    private FragmentCheckout3Binding binding;
    OrderModel orderModel;
    TextView merchant, amount, total;
    String store_name;
    float expense;
    String dateTimeString;
    int id;
    public static String name = "";
    public static String email = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCheckout3Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        amount = root.findViewById(R.id.tv_amount3);
        merchant = root.findViewById(R.id.tv_merchantname);
        total = root.findViewById(R.id.tv_total_amount);

        Bundle bundle = getArguments();
        if (bundle != null){
            name = bundle.getString("name");
            id = bundle.getInt("id");
            email = bundle.getString("email");
            expense = bundle.getFloat("expense");
            dateTimeString = bundle.getString("datetime");
            merchant.setText("Mosibus");
            //merchant.setText(orderModel.getStore_name());
            amount.setText(String.valueOf(expense));
            total.setText(String.valueOf(expense));
        }


        binding.nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putInt("id", id);
                bundle.putString("email", email);
                bundle.putFloat("expense", expense);
                bundle.putString("datetime", dateTimeString);
                Checkout4Fragment fragment = new Checkout4Fragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();
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