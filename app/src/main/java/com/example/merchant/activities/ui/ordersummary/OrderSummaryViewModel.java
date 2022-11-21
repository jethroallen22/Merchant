package com.example.merchant.activities.ui.ordersummary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderSummaryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OrderSummaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is products fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}