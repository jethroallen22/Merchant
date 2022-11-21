package com.example.merchant.activities.ui.addproduct;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddProductViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add product fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}